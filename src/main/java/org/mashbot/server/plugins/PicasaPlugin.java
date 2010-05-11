package org.mashbot.server.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mashbot.server.exceptions.IncompleteSecretInformationException;
import org.mashbot.server.exceptions.InvalidConfigFileException;
import org.mashbot.server.exceptions.InvalidFieldException;
import org.mashbot.server.exceptions.InvalidRequestException;
import org.mashbot.server.exceptions.MashbotException;
import org.mashbot.server.exceptions.MissingAuthenticationException;
import org.mashbot.server.exceptions.UndownloadableContentException;
import org.mashbot.server.ext.picasa.PicasaWebClient;
import org.mashbot.server.ext.picasa.PicasaWebCommandLine;
import org.mashbot.server.types.MObject;
import org.mashbot.server.types.ServiceCredential;

import com.aetrion.flickr.uploader.UploadMetaData;
import com.google.gdata.client.Service.GDataRequest.RequestType;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthRsaSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthSigner;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.OtherContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.data.media.MediaStreamSource;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.TagEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class PicasaPlugin extends Plugin {
	
	private static String serviceName = "picasa";
	private static Log log = LogFactory.getLog(FlickrPlugin.class);
	private static final Map<String, List<String>> supported;
	private static final List<String> required;
    static {
        Map<String, List<String>> aSupported = new HashMap<String,List<String>>();
        List<String> photos = new ArrayList<String>();
		photos.add("push");
		photos.add("pull");
		photos.add("edit");
		photos.add("delete");
		aSupported.put("photo", Collections.unmodifiableList(photos));
        supported = Collections.unmodifiableMap(aSupported);
        
        List<String> aRequired = new ArrayList<String>();
        aRequired.add(MObject.Field.URL.toString());
        aRequired.add(MObject.Field.TITLE.toString());
        aRequired.add(MObject.Field.ALBUM.toString());
        required = Collections.unmodifiableList(aRequired);
    }

	private PicasawebService picasaService;
	private PicasaWebCommandLine picasaClient;

	@Override
	protected MObject delete(String contentType, MObject content)
			throws MashbotException, IncompleteSecretInformationException,
			InvalidConfigFileException, InvalidFieldException,
			UndownloadableContentException, InvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected MObject edit(String contentType, MObject content)
			throws MashbotException, IncompleteSecretInformationException,
			InvalidConfigFileException, InvalidFieldException,
			UndownloadableContentException, InvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getRequiredInformation(String operation,
			String contentType) {
		return this.required;
	}

	@Override
	public String getServiceName() {
		return PicasaPlugin.serviceName;
	}

	@Override
	public Map<String, List<String>> getSupported() {
		return this.supported;
	}

	@Override
	protected MObject pull(String contentType, MObject content)
			throws MashbotException, IncompleteSecretInformationException,
			InvalidConfigFileException, InvalidFieldException,
			UndownloadableContentException, InvalidRequestException { 
		
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	protected MObject push(String contentType, MObject content)
			throws MashbotException, IncompleteSecretInformationException,
			InvalidConfigFileException, InvalidFieldException,
			UndownloadableContentException, InvalidRequestException {
		
		AlbumEntry album;
		MediaContent data = new MediaContent();
		
		String title = content.getStringField(MObject.Field.TITLE);
		
		try {
			String albumtitle,albumdesc;
			if(content.containsField(MObject.Field.ALBUM)){
				if(content.containsField(MObject.Field.ALBUMDESC)){
					albumtitle = content.getStringField(MObject.Field.ALBUM);
					albumdesc = content.getStringField(MObject.Field.ALBUMDESC);
				} else {
					albumtitle = content.getStringField(MObject.Field.ALBUM);
					albumdesc = "";
				}
			} else {
				albumtitle = title;
				albumdesc = "";
			}
			
			//this.picasaService.
			album = this.picasaClient.fetchOrCreateAlbum(albumtitle, albumdesc);
			
			
		} catch (IOException e) {
			throw new InvalidRequestException(e);
		} catch (ServiceException e) {
			log.warn(e.getDebugInfo());
			log.warn(e.getExtendedHelp());
			log.warn(e.getMessage());
			log.warn(e.getInternalReason());
			throw new InvalidRequestException(e);
		} 
		
		PhotoEntry photo = new PhotoEntry();
		
		String urlString = content.getStringField(MObject.Field.URL);
		URL url;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			throw new InvalidFieldException(MObject.Field.URL);
		}
		
		InputStream in;
		String mimeType;
		
		try {
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(0);
			connection.setReadTimeout(0);
			in = connection.getInputStream();
			mimeType = connection.getContentType();
		} catch (IOException e) {
			throw new UndownloadableContentException(); 
		}
		
		data.setMediaSource(new MediaStreamSource(in,mimeType));
		photo.setContent(data);
		
		photo.setTitle(new PlainTextConstruct(title));
		log.warn("HOLY FUCK ITS A TITLE:" + title);
		
		if(content.containsField(MObject.Field.CAPTION)){
			String caption = content.getStringField(MObject.Field.CAPTION);
			photo.setDescription(new PlainTextConstruct(caption));
		}
		
		try {
			photo = this.picasaClient.insert(album,photo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(content.containsField(MObject.Field.TAGS)){
			Collection<String> tags = content.getField(MObject.Field.TAGS);
			for(String i : tags) {
				TagEntry tag = new TagEntry();
				tag.setTitle(new PlainTextConstruct(i));
								
				try {
					this.picasaClient.insert(photo,tag);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return content;		
	}
	

	@Override
	protected void setup(ServiceCredential credential, String operation)
			throws IncompleteSecretInformationException,
			InvalidConfigFileException, MissingAuthenticationException {
		 this.picasaService = new PicasawebService("Mashbot-Mashbot-1.0");
		 
		 if(credential.method.equals("oauth")){
			 OAuthParameters params = new OAuthParameters();
			 log.warn("token:"+credential.key);
			 log.warn("tokenSecret:"+credential.secret);
			 params.setOAuthToken(credential.key);
			 params.setOAuthTokenSecret(credential.secret);
			 params.setOAuthConsumerKey("www.mashbot.net");
			 params.setOAuthConsumerSecret("gQm5ijNqtZ6qwuWpRbENKdso");
			 OAuthSigner signer = new OAuthHmacSha1Signer();
			 try {
				this.picasaService.setOAuthCredentials(params,signer);
			} catch (OAuthException e) {
				throw new MissingAuthenticationException();
			}
			log.warn("Auth Token:" + this.picasaService.getAuthTokenFactory().getAuthToken());
		 } else if(credential.method.equals("userpass")) {
			 try {
				this.picasaService.setUserCredentials(credential.key, credential.secret);
			} catch (AuthenticationException e) {
				throw new MissingAuthenticationException();
			}
		 }
		 	
		 this.picasaClient = new PicasaWebCommandLine(this.picasaService);
	}

	public static void main(String [] args){
		PicasaPlugin in = new PicasaPlugin();
		
		MObject hooray = new MObject();
		hooray.putField(MObject.Field.URL, "http://3.bp.blogspot.com/_JEfe6AelcdQ/R37vGY0tBSI/AAAAAAAAAYE/oru4LgbUpGM/s320/_41186601_hooray-pa5.jpg");
		hooray.putField(MObject.Field.TITLE,"Epic Freezer");
		
		ServiceCredential credential = new ServiceCredential();
		credential.key = "1/50Slookfrhux4POAtNOTkKeDhoQj4nmM89aKt7fGjzo";
		credential.secret = "PoB1LhK4QEXPiukkQPGAoABH";
		credential.method = "oauth";
		
		try {
			MObject herro = in.run("push", "photo", hooray, credential);
			log.warn(herro);
		} catch (IncompleteSecretInformationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UndownloadableContentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MashbotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
