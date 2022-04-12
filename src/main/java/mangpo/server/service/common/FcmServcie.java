package mangpo.server.service.common;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcmServcie {

    private final S3Service s3Service;

    public void send_FCM(String tokenId, String title, String content) throws IOException, FirebaseMessagingException {
            InputStream serviceAccount = s3Service.getObjectInputStream("config/ourpage-firebase-adminsdk-6g3xq-9a3461d29b.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            //Firebase 처음 호출시에만 initializing 처리
            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            
            //String registrationToken = 안드로이드 토큰 입력;
            String registrationToken = tokenId;

            //message 작성
            Message msg = Message.builder()
                    .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000) // 1 hour in milliseconds
                        .setPriority(AndroidConfig.Priority.NORMAL)
                        .setNotification(AndroidNotification.builder()
                            .setTitle(title)
                            .setBody(content)
                            .setIcon("stock_ticker_update")
                            .setColor("#f45342")
                            .build())
                        .build())
                    .setToken(registrationToken)
                    .build();

            //메세지를 FirebaseMessaging 에 보내기
            String response = FirebaseMessaging.getInstance().send(msg);
            //결과 출력
            log.info("Successfully sent message:{}",response);
    }
}