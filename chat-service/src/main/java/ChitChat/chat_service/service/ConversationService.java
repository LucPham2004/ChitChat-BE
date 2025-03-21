package ChitChat.chat_service.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ChitChat.chat_service.dto.request.ConversationRequest;
import ChitChat.chat_service.dto.response.ChatParticipants;
import ChitChat.chat_service.dto.response.UserResponse;
import ChitChat.chat_service.entity.Conversation;
import ChitChat.chat_service.exception.AppException;
import ChitChat.chat_service.exception.ErrorCode;
import ChitChat.chat_service.mapper.ConversationMapper;
import ChitChat.chat_service.repository.ConversationRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class ConversationService {
    
    ConversationRepository conversationRepository;
    //MessageRepository messageRepository;
    ConversationMapper conversationMapper;
    UserServiceClient userServiceClient;
    
    static int CONVERSATIONS_PER_PAGE = 20;

    // GET METHODS

    public Conversation getById(Long id) {
        return conversationRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
    }
    
    // Get direct message conversation between two users, if not exists, create new one
    public Conversation getDirectMessage(Long selfId, Long otherId) {
        Conversation conversation = conversationRepository.findDirectMessage(selfId, otherId).orElse(null);
        
        if(conversation == null) {
            ConversationRequest conversationRequest = new ConversationRequest();

            Set<Long> participantIds = new HashSet<>();
            participantIds.add(selfId);
            participantIds.add(otherId);
            conversationRequest.setParticipantIds(participantIds);
            conversationRequest.setOwnerId(selfId);
            conversationRequest.setEmoji("👍");
            conversationRequest.setGroup(false);

            conversation = createConversation(conversationRequest);
        }

        return conversation;
    }

    public Page<Conversation> getByParticipantId(Long userId, int pageNum) {
        if(userServiceClient.getUserById(userId).getResult() == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, CONVERSATIONS_PER_PAGE);

        return conversationRepository.findByParticipantIdsContaining(userId, pageable);
    }

    public Page<Conversation> getByOwnerId(Long userId, int pageNum) {
        if(userServiceClient.getUserById(userId).getResult() == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, CONVERSATIONS_PER_PAGE);

        return conversationRepository.findByOwnerId(userId, pageable);
    }

    public List<ChatParticipants> getParticipantsByConvId(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));

        List<ChatParticipants> list = new ArrayList<>();

        for(Long userId: conversation.getParticipantIds()) {
            UserResponse user = userServiceClient.getUserById(userId).getResult();
            
            ChatParticipants participant = ChatParticipants.builder()
                .id(user.getId())
                .username(user.getUsername())
                .avatarPublicId(user.getAvatarPublicId())
                .avatarUrl(user.getAvatarUrl())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

            list.add(participant);
        }
        
        return list;
    }

    public Long getDirectMessageId(Long selfId, Long otherId) {
        Long conversationId = conversationRepository.findDirectMessageId(selfId, otherId);
        
        if(conversationId == null) {
            ConversationRequest conversationRequest = new ConversationRequest();

            Set<Long> participantIds = new HashSet<>();
            participantIds.add(selfId);
            participantIds.add(otherId);
            conversationRequest.setParticipantIds(participantIds);
            conversationRequest.setOwnerId(selfId);
            conversationRequest.setEmoji("👍");
            conversationRequest.setGroup(false);

            conversationId = createConversation(conversationRequest).getId();
        }

        return conversationId;
    }

    // POST METHODS

    public Conversation createConversation(ConversationRequest conversationRequest) {
        return conversationRepository.save(conversationMapper.toConversation(conversationRequest));
    }

    public List<Conversation> createManyConversations(List<ConversationRequest> conversationRequests) {
        List<Conversation> newConversations = conversationRequests.stream()
            .map(conversationMapper::toConversation)
            .map(conversationRepository::save)
            .toList();
        return newConversations;
    }

    // PUT METHODS

    @Transactional
    public Conversation updateConversation(ConversationRequest conversationRequest) {
        Conversation conversation = conversationRepository.findById(conversationRequest.getId()).get();

        if(conversationRequest.getName() != null && !conversationRequest.getName().isEmpty()
            && !conversationRequest.getName().equals(conversation.getName())) {
            conversation.setName(conversationRequest.getName());
        }

        if(conversationRequest.getDescription() != null && !conversationRequest.getDescription().isEmpty()
            && !conversationRequest.getDescription().equals(conversation.getDescription())) {
            conversation.setDescription(conversationRequest.getDescription());
        }

        if(conversationRequest.getColor() != null && !conversationRequest.getColor().isEmpty()
            && !conversationRequest.getColor().equals(conversation.getColor())) {
            conversation.setColor(conversationRequest.getColor());
        }

        if(conversationRequest.getEmoji() != null && !conversationRequest.getEmoji().isEmpty()
            && !conversationRequest.getEmoji().equals(conversation.getEmoji())) {
            conversation.setEmoji(conversationRequest.getEmoji());
        }

        if(conversationRequest.getParticipantIds() != null && !conversationRequest.getParticipantIds().isEmpty()
            && !conversationRequest.getParticipantIds().equals(conversation.getParticipantIds())) {
            conversation.setParticipantIds(conversationRequest.getParticipantIds());
        }

        if(conversationRequest.getOwnerId() != null
            && !conversationRequest.getOwnerId().equals(conversation.getOwnerId())) {
            conversation.setOwnerId(conversationRequest.getOwnerId());
        }

        if(conversationRequest.isGroup() != conversation.isGroup()) {
            conversation.setGroup(conversationRequest.isGroup());
        }

        if(conversationRequest.isRead() != conversation.isRead()) {
            conversation.setRead(conversationRequest.isRead());
        }

        if(conversationRequest.isMuted() != conversation.isMuted()) {
            conversation.setMuted(conversationRequest.isMuted());
        }

        if(conversationRequest.isPinned() != conversation.isPinned()) {
            conversation.setPinned(conversationRequest.isPinned());
        }

        if(conversationRequest.isArchived() != conversation.isArchived()) {
            conversation.setArchived(conversationRequest.isArchived());
        }

        if(conversationRequest.isDeleted() != conversation.isDeleted()) {
            conversation.setDeleted(conversationRequest.isDeleted());
        }

        if(conversationRequest.isBlocked() != conversation.isBlocked()) {
            conversation.setBlocked(conversationRequest.isBlocked());
        }

        if(conversationRequest.isReported() != conversation.isReported()) {
            conversation.setReported(conversationRequest.isReported());
        }

        if(conversationRequest.isSpam() != conversation.isSpam()) {
            conversation.setSpam(conversationRequest.isSpam());
        }

        if(conversationRequest.isMarkedAsUnread() != conversation.isMarkedAsUnread()) {
            conversation.setMarkedAsUnread(conversationRequest.isMarkedAsUnread());
        }

        if(conversationRequest.isMarkedAsRead() != conversation.isMarkedAsRead()) {
            conversation.setMarkedAsRead(conversationRequest.isMarkedAsRead());
        }

        return conversationRepository.save(conversation);
    }

    // Update conversation partially
    @Transactional
    public Conversation updateConversationPartially(Long id, Map<String, Object> updates) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    String name = (String) value;
                    if (name != null && !name.isEmpty() && !name.equals(conversation.getName())) {
                        conversation.setName(name);
                    }
                    break;

                case "description":
                    String description = (String) value;
                    if (description != null && !description.isEmpty() && !description.equals(conversation.getDescription())) {
                        conversation.setDescription(description);
                    }
                    break;

                case "color":
                    String color = (String) value;
                    if (color != null && !color.isEmpty() && !color.equals(conversation.getColor())) {
                        conversation.setColor(color);
                    }
                    break;

                case "emoji":
                    String emoji = (String) value;
                    if (emoji != null && !emoji.isEmpty() && !emoji.equals(conversation.getEmoji())) {
                        conversation.setEmoji(emoji);
                    }
                    break;

                case "avatarPublicId":
                    String publicId = (String) value;
                    if (publicId != null && !publicId.isEmpty() && !publicId.equals(conversation.getAvatarPublicId())) {
                        conversation.setAvatarPublicId(publicId);
                    }
                    break;

                case "avatarUrl":
                    String avatarUrl = (String) value;
                    if (avatarUrl != null && !avatarUrl.isEmpty() && !avatarUrl.equals(conversation.getAvatarUrl())) {
                        conversation.setAvatarUrl(avatarUrl);
                    }
                    break;

                case "participantIds":
                    @SuppressWarnings("unchecked")
                    List<Integer> participantList = (List<Integer>) value; // Jackson chuyển Set<Long> thành List<Integer>
                    Set<Long> participantIds = participantList.stream()
                            .map(Long::valueOf)
                            .collect(Collectors.toSet());
                    conversation.setParticipantIds(participantIds);
                    break;

                case "ownerId":
                    Long ownerId = ((Number) value).longValue();
                    if (!ownerId.equals(conversation.getOwnerId())) {
                        conversation.setOwnerId(ownerId);
                    }
                    break;

                case "isGroup":
                    boolean isGroup = (Boolean) value;
                    if (isGroup != conversation.isGroup()) {
                        conversation.setGroup(isGroup);
                    }
                    break;

                case "isRead":
                    boolean isRead = (Boolean) value;
                    if (isRead != conversation.isRead()) {
                        conversation.setRead(isRead);
                    }
                    break;

                case "isMuted":
                    boolean isMuted = (Boolean) value;
                    if (isMuted != conversation.isMuted()) {
                        conversation.setMuted(isMuted);
                    }
                    break;

                case "isPinned":
                    boolean isPinned = (Boolean) value;
                    if (isPinned != conversation.isPinned()) {
                        conversation.setPinned(isPinned);
                    }
                    break;

                case "isArchived":
                    boolean isArchived = (Boolean) value;
                    if (isArchived != conversation.isArchived()) {
                        conversation.setArchived(isArchived);
                    }
                    break;

                case "isDeleted":
                    boolean isDeleted = (Boolean) value;
                    if (isDeleted != conversation.isDeleted()) {
                        conversation.setDeleted(isDeleted);
                    }
                    break;

                case "isBlocked":
                    boolean isBlocked = (Boolean) value;
                    if (isBlocked != conversation.isBlocked()) {
                        conversation.setBlocked(isBlocked);
                    }
                    break;

                case "isReported":
                    boolean isReported = (Boolean) value;
                    if (isReported != conversation.isReported()) {
                        conversation.setReported(isReported);
                    }
                    break;

                case "isSpam":
                    boolean isSpam = (Boolean) value;
                    if (isSpam != conversation.isSpam()) {
                        conversation.setSpam(isSpam);
                    }
                    break;

                case "isMarkedAsUnread":
                    boolean isMarkedAsUnread = (Boolean) value;
                    if (isMarkedAsUnread != conversation.isMarkedAsUnread()) {
                        conversation.setMarkedAsUnread(isMarkedAsUnread);
                    }
                    break;

                case "isMarkedAsRead":
                    boolean isMarkedAsRead = (Boolean) value;
                    if (isMarkedAsRead != conversation.isMarkedAsRead()) {
                        conversation.setMarkedAsRead(isMarkedAsRead);
                    }
                    break;

                default:
                    System.out.println("Trường không hợp lệ: " + key);
            }
        });

        return conversationRepository.save(conversation);
    }


    // DELETE METHODS

    public void deleteConversation(Conversation conversation) {
        conversationRepository.delete(conversation);
    }

    public void deleteConversationById(Long id) {
        conversationRepository.deleteById(id);
    }

    // OTHER METHODS
    public boolean existsById(Long id) {
        return conversationRepository.existsById(id);
    }

    public int countByOwnerId(Long userId) {
        return conversationRepository.countByOwnerId(userId);
    }
}
