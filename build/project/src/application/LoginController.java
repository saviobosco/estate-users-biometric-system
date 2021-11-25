package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import javafx.event.ActionEvent;

import javafx.scene.control.PasswordField;

public class LoginController implements Initializable {
	@FXML
	private Button btnLogin;
	@FXML
	private TextField textUsername;
	@FXML
	private PasswordField textPassword;

	private final BasicCookieStore cookieStore = new BasicCookieStore();
	
	private final CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
			.build();
	
	public void initialize(URL url, ResourceBundle rb) {
		System.out.println("hello");
		
		try {
			
			// Create a custom response handler
            final HttpClientResponseHandler<String> responseHandler = new HttpClientResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final ClassicHttpResponse response) throws IOException {
                    final int status = response.getCode();
                    if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                        final HttpEntity entity = response.getEntity();
                        try {
                            return entity != null ? EntityUtils.toString(entity) : null;
                        } catch (final ParseException ex) {
                            throw new ClientProtocolException(ex);
                        }
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
			
			
			
			final HttpGet httpget = new HttpGet("http://griffontech-estate-manager.herokuapp.com/login");
			
			final String responseBody = httpclient.execute(httpget, responseHandler);
            /*System.out.println("----------------------------------------");
            System.out.println(responseBody);
            System.out.println("Initial set of cookies:");
			final List<Cookie> cookies = cookieStore.getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i));
				}
			}*/
			
			/*try (final CloseableHttpResponse response1 = httpclient.execute(httpget)) {
				final HttpEntity entity = response1.getEntity();
				
				System.out.println("Login form get : " + response1.getCode() + " " + response1.getReasonPhrase());
				EntityUtils.consume(entity);
				
				System.out.println("Initial set of cookies:");
				final List<Cookie> cookies = cookieStore.getCookies();
				if (cookies.isEmpty()) {
					System.out.println("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						System.out.println("- " + cookies.get(i));
					}
				}
			} catch (UnknownHostException e) {
				System.out.println("No internet connection");
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// Event Listener on Button[#btnLogin].onAction
	@FXML
	public void handleButtonAction(ActionEvent event) {
		if (event.getSource() == btnLogin) {
			String username = textUsername.getText();
			String password = textPassword.getText();
			
			// Create a custom response handler
            final HttpClientResponseHandler<String> responseHandler = new HttpClientResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final ClassicHttpResponse response) throws IOException {
                	
                    final int status = response.getCode();
                    System.out.println(status);
                    
                    if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                        final HttpEntity entity = response.getEntity();
                        try {
                            return entity != null ? EntityUtils.toString(entity) : null;
                        } catch (final ParseException ex) {
                            throw new ClientProtocolException(ex);
                        }
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
			
			/*try {

				final ClassicHttpRequest login = ClassicRequestBuilder.post()
	                    .setUri(new URI("http://griffontech-estate-manager.herokuapp.com/login"))
	                    .addParameter("email", username)
	                    .addParameter("password", password)
	                    .build();
				
				
				try (final CloseableHttpResponse response2 = httpclient.execute(login)) {
	                final HttpEntity entity = response2.getEntity();
	                
	                try {
                        String responseString = entity != null ? EntityUtils.toString(entity) : null;
                    } catch (final ParseException ex) {
                        throw new ClientProtocolException(ex);
                    }
	                
	                System.out.println("Login form get: " + response2.getCode() + " " + response2.getReasonPhrase());
	                EntityUtils.consume(entity);

	                System.out.println("Post logon cookies:");
	                
	                final List<Cookie> cookies = cookieStore.getCookies();
	                if (cookies.isEmpty()) {
	                    System.out.println("None");
	                } else {
	                    for (int i = 0; i < cookies.size(); i++) {
	                        System.out.println("- " + cookies.get(i));
	                    }
	                }
	            }
				
				
				
								
				final String responseBody = httpclient.execute(login, responseHandler);
	            System.out.println("----------------------------------------");
	            System.out.println(responseBody);
				
	            
	            System.out.println("cookies:");
				final List<Cookie> cookies = cookieStore.getCookies();
				if (cookies.isEmpty()) {
					System.out.println("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						System.out.println("- " + cookies.get(i));
					}
				}
				
				// comment this place later
				
				try (final CloseableHttpResponse response2 = httpclient.execute(login)) {
	                final HttpEntity entity = response2.getEntity();

	                System.out.println("Login form get: " + response2.getCode() + " " + response2.getReasonPhrase());
	                
	                
	                EntityUtils.consume(entity);

	                System.out.println("Post logon cookies:");
	                if (cookies.isEmpty()) {
	                    System.out.println("None");
	                } else {
	                    for (int i = 0; i < cookies.size(); i++) {
	                        System.out.println("- " + cookies.get(i).getValue());
	                    }
	                }
	            }
				// commenting ends here.
				
			} catch (UnknownHostException e) {
				System.out.println("No internet connection");
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			
			
			if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
				System.out.println("Login Successful.");
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Login was successful.");
				alert.showAndWait();
				
				Stage currentStage = (Stage) textUsername.getScene().getWindow();
				currentStage.close();
				Stage primaryStage = new Stage();
				try {
					Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
					Scene scene = new Scene(root);
					primaryStage.setScene(scene);
					primaryStage.setTitle("Estate-Manager Application -by Griffon Technologies Nig.");
					primaryStage.show();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Username or password incorrect!.");
				alert.show();
			}
		}
	}
	
	
	public String close() {
		return "";
	}
}
