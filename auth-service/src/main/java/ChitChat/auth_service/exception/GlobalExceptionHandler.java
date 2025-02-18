// package ChitChat.auth_service.exception;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;

// import ChitChat.auth_service.dto.response.ApiResponse;


// @ControllerAdvice
// public class GlobalExceptionHandler {
    
//     @ExceptionHandler(value = Exception.class)
//     public ResponseEntity<ApiResponse<String>> handlingException(Exception exception) {
//         ApiResponse<String> apiResponse = new ApiResponse<>();

//         apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
//         apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
//     }

//     @ExceptionHandler(value = AppException.class)
//     public ResponseEntity<ApiResponse<String>> handleAppException(AppException exception) {
//         ErrorCode errorCode = exception.getErrorCode();

//         ApiResponse<String> apiResponse = ApiResponse.<String>builder()
//                 .code(errorCode.getCode())
//                 .message(errorCode.getMessage())
//                 .result(exception.getMessage())
//                 .build();

//         return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
//     }

//     @ExceptionHandler(value = MethodArgumentNotValidException.class)
//     public ResponseEntity<ApiResponse<String>> handlingValidation(MethodArgumentNotValidException exception) {
//         String enumKey = exception.getFieldError().getDefaultMessage();

//         ErrorCode errorCode = ErrorCode.INVALID_KEY;

//         try {
//             errorCode = ErrorCode.valueOf(enumKey);
//         } catch(IllegalArgumentException e) {

//         }

//         ApiResponse<String> apiResponse = new ApiResponse<>();

//         apiResponse.setCode(errorCode.getCode());
//         apiResponse.setMessage(errorCode.getMessage());
        
//         return ResponseEntity.badRequest().body(apiResponse);
//     }
// }
