package com.TruckBooking.routeData.Exception;

import com.TruckBooking.LoadsApi.Exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@ControllerAdvice
public class RouteDataExceptionAdvice extends ResponseEntityExceptionHandler {

	/**
	 * Handle MissingServletRequestParameterException. Triggered when a 'required'
	 * request parameter is missing.
	 *
	 * @param ex      MissingServletRequestParameterException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("handleMissingServletRequestParameter is started");
		String error = ex.getParameterName() + " parameter is missing";
		return buildResponseEntity(new RouteDataErrorResponse(HttpStatus.BAD_REQUEST, error, ex));
	}

	/**
	 * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is
	 * invalid as well.
	 *
	 * @param ex      HttpMediaTypeNotSupportedException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("handleHttpMediaTypeNotSupported is started");
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
		return buildResponseEntity(new RouteDataErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
				builder.substring(0, builder.length() - 2), ex));
	}

	/**
	 * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid
	 * validation.
	 *
	 * @param ex      the MethodArgumentNotValidException that is thrown when @Valid
	 *                validation fails
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("handleMethodArgumentNotValid is started");
		RouteDataErrorResponse routeDataErrorResponse = new RouteDataErrorResponse(HttpStatus.BAD_REQUEST);
		routeDataErrorResponse.setMessage("Validation error");
		routeDataErrorResponse.addValidationErrors(ex.getBindingResult().getFieldErrors());
		routeDataErrorResponse.addValidationError(ex.getBindingResult().getGlobalErrors());
		return buildResponseEntity(routeDataErrorResponse);
	}

	/**
	 * Handles javax.validation.ConstraintViolationException. Thrown when @Validated
	 * fails.
	 *
	 * @param ex the ConstraintViolationException
	 * @return the ApiError object
	 */
	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
		log.error("handleConstraintViolation is started");
		RouteDataErrorResponse routeDataErrorResponse = new RouteDataErrorResponse(HttpStatus.BAD_REQUEST);
		routeDataErrorResponse.setMessage("Validation error");
		routeDataErrorResponse.addValidationErrors(ex.getConstraintViolations());
		return buildResponseEntity(routeDataErrorResponse);
	}

	/**
	 * Handles EntityNotFoundException. Created to encapsulate errors with more
	 * detail than javax.persistence.EntityNotFoundException.
	 *
	 * @param ex the EntityNotFoundException
	 * @return the ApiError object
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
		log.error("handleEntityNotFound is started");
		RouteDataErrorResponse routeDataErrorResponse = new RouteDataErrorResponse(HttpStatus.NOT_FOUND);
		routeDataErrorResponse.setMessage(ex.getMessage());
		return buildResponseEntity(routeDataErrorResponse);
	}

	/**
	 * Handle HttpMessageNotReadableException. Happens when request JSON is
	 * malformed.
	 *
	 * @param ex      HttpMessageNotReadableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("handleHttpMessageNotReadable is started");
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
		String error = "Malformed JSON request";
		return buildResponseEntity(new RouteDataErrorResponse(HttpStatus.BAD_REQUEST, error, ex));
	}

	/**
	 * Handle HttpMessageNotWritableException.
	 *
	 * @param ex      HttpMessageNotWritableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("handleHttpMessageNotWritable is started");
		String error = "Error writing JSON output";
		return buildResponseEntity(new RouteDataErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
	}

	/**
	 * Handle NoHandlerFoundException.
	 *
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		log.error("handleNoHandlerFoundException is started");
		RouteDataErrorResponse routeDataErrorResponse = new RouteDataErrorResponse(HttpStatus.BAD_REQUEST);
		routeDataErrorResponse.setMessage(
				String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
		routeDataErrorResponse.setDebugMessage(ex.getMessage());
		return buildResponseEntity(routeDataErrorResponse);
	}

	/**
	 * Handle javax.persistence.EntityNotFoundException
	 */
	@ExceptionHandler(javax.persistence.EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
		log.error("handleEntityNotFound is started");
		return buildResponseEntity(new RouteDataErrorResponse(HttpStatus.NOT_FOUND, ex));
	}

	/**
	 * Handle DataIntegrityViolationException, inspects the cause for different DB
	 * causes.
	 *
	 * @param ex the DataIntegrityViolationException
	 * @return the ApiError object
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
			WebRequest request) {
		log.error("handleDataIntegrityViolation is started");
		if (ex.getCause() instanceof ConstraintViolationException) {
			return buildResponseEntity(new RouteDataErrorResponse(HttpStatus.CONFLICT, "Database error", ex.getCause()));
		}
		return buildResponseEntity(new RouteDataErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex));
	}

	/**
	 * Handle Exception, handle generic Exception.class
	 *
	 * @param ex the Exception
	 * @return the ApiError object
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		log.error("handleMethodArgumentTypeMismatch is started");
		RouteDataErrorResponse routeDataErrorResponse = new RouteDataErrorResponse(HttpStatus.BAD_REQUEST);
		routeDataErrorResponse
				.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
						ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
		routeDataErrorResponse.setDebugMessage(ex.getMessage());
		return buildResponseEntity(routeDataErrorResponse);
	}

	@ExceptionHandler(com.TruckBooking.LoadsApi.Exception.BusinessException.class)
	protected ResponseEntity<Object> handleBusinessException(BusinessException ex) {
		log.error("handleBusiness Exception is started");
		RouteDataErrorResponse routeDataErrorResponse = new RouteDataErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY);
		routeDataErrorResponse.setMessage(ex.getMessage());
		return buildResponseEntity(routeDataErrorResponse);
	}

	@ExceptionHandler(Exception.class)  
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request)  
	{  
		log.error("handleAllExceptions is started");
		return buildResponseEntity(new RouteDataErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR , ex));
	}  

	private ResponseEntity<Object> buildResponseEntity(RouteDataErrorResponse routeDataErrorResponse) {
		return new ResponseEntity<>(routeDataErrorResponse, routeDataErrorResponse.getStatus());
	}
}
