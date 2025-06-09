package ru.ya.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.nio.channels.ClosedChannelException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public String handleServiceIsUnavailable(final HttpServerErrorException.InternalServerError e) {
        return "service-is-unavailable.html";
    }

    @ExceptionHandler
    public String handleServiceIsUnavailable2(final ResourceAccessException e) {
        return "service-is-unavailable.html";
    }

    @ExceptionHandler
    public String handleServiceIsUnavailable3(final ClosedChannelException e) {
        return "service-is-unavailable.html";
    }

    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDuplicateUserName(final DuplicateUserNameException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Это имя уже занято другим пользователем",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleViolationUserRestrictions(final ViolationUserRestrictionsException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Нарушение целостности данных",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFound(final UserNotFoundException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIncorrectCategoryRequest(final IncorrectCategoryRequestException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "У добавляемого пользователя заданы некорректные данные",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCategoryNotFound(final CategoryNotFoundException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDuplicateCategoryName(final DuplicateCategoryNameException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Категория с таким именем уже существует",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRemovingCategory(final RemovingCategoryException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Нельзя удалить данную категорию, т.к. к ней уже привязаны события",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEventNotFound(final EventNotFoundException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIncorrectEventRequest(final IncorrectEventRequestException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Некорректная дата начала события",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCanceledEvent(final CanceledEventException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Нельзя опубликовать отмененное событие",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlePatchRestriction(final PatchRestrictionException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Нельзя вносить изменения в уже опубликованные события",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleParticipationRequestNotFound(final ParticipationRequestNotFoundException e, long userId) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Request with id= " + userId + " was not found",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleParticipantLimitAchieved(final ParticipantLimitAchievedException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Уже достигнут лимит одобренных заявок на мероприятие",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleViolationParticipationRequestStatus(final ViolationParticipationRequestStatusException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Можно изменять статус только у заявок, находящихся в состоянии ожидания",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIncorrectParams(final IncorrectParamsException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Введены некорректные параметры запроса",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDuplicateParticipationRequest(final DuplicateParticipationRequestException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Нельзя отправлять повторный запрос от того же пользователя на то же событие",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequesterMatchesInitiator(final RequesterMatchesInitiatorException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Инициатор события не может участвовать в собственном событии",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUnacceptableEventStatusForParticipation(
            final UnacceptableEventStatusForParticipationException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Нельзя принять участие в еще не опубликованном событии",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIncorrectCompilationRequest(final IncorrectCompilationRequestException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Некорректная длина заголовка",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCompilationNotFound(final CompilationNotFoundException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Подборка не найдена",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleIncorrectTextLength(final IncorrectTextLengthException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Длина текста < 20 либо > 5000 символов",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCommentNotFound(final CommentNotFoundException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Комментарий с указанным id не найден",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNoRightsToUpdate(final NoRightsToUpdateException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Комментарий оставлен не данным пользователем - он не имеет прав на редактирование",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNoRightsToPost(final NoRightsToPostException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Пользователь не может оставлять комментарии к организуемому им же событию",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleNotPermittedStateToPostAndUpdate(final NotPermittedStateToPostAndUpdateException e) {
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Добавлять и редактировать комментарии можно только к опубликованным событиям",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }*/
}