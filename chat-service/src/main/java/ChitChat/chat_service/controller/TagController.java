package ChitChat.chat_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ChitChat.chat_service.dto.response.ApiResponse;
import ChitChat.chat_service.entity.Tag;
import ChitChat.chat_service.service.TagService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class TagController {
    TagService tagService;

    @GetMapping("/get/all")
    public ApiResponse<Page<Tag>> getAllTags(@RequestParam(defaultValue = "0") int pageNum) {
        return ApiResponse.<Page<Tag>>builder()
            .code(1000)
            .message("Get tags successfully")
            .result(tagService.getAllTags(pageNum))
            .build();
    }

    @GetMapping("/get/popular")
    public ApiResponse<Page<Tag>> getAllTagsSortedByPostCount(@RequestParam(defaultValue = "0") int pageNum) {
        return ApiResponse.<Page<Tag>>builder()
            .code(1000)
            .message("Get tags successfully")
            .result(tagService.getAllTags(pageNum))
            .build();
    }
    
    @PostMapping("/create")
    public ApiResponse<Tag> createTag(String nameTag) {
        return ApiResponse.<Tag>builder()
            .code(1000)
            .message("Create Tag successfully")
            .result(tagService.createTag(nameTag))
            .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Delete Tag successfully")
            .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteTag(@RequestBody Tag tag) {
        tagService.deleteTag(tag);
        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Delete Tag successfully")
            .build();
    }
}
