package ChitChat.notification_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import ChitChat.notification_service.dto.UserDTO;
import ChitChat.notification_service.entity.Notification;
import ChitChat.notification_service.entity.NotificationType;
import ChitChat.notification_service.exception.AppException;
import ChitChat.notification_service.exception.ErrorCode;
import ChitChat.notification_service.repository.NotificationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class NotificationService {

    //SimpMessagingTemplate messagingTemplate;
    NotificationRepository notificationRepository;
    UserServiceClient userServiceClient;

    static int NOTIFY_PER_PAGE = 10;

    // Get User Notifications
    public Page<Notification> getUserNotifications(Long userId, int pageNum) {
        UserDTO user = userServiceClient.getUserById(userId).getResult();
        if(user == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, NOTIFY_PER_PAGE);
        return notificationRepository.findByUserId(userId, pageable);
    }

    // Create Notification
    public Notification notifyUser(String username, String message, NotificationType type) {
        //
        return null;
    }

    // Delete Notification
    public void deleteNotify(Long notifyId) {
        if (!notificationRepository.existsById(notifyId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        notificationRepository.deleteById(notifyId);
    }
}
