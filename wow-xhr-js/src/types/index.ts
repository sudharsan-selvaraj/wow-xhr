enum HttpMethod {
    GET = "GET",
    POST = "POST",
    PUT = "PUT",
    PATCH = "PATCH",
    DELETE = "DELETE",
}

interface XhookRequest {
    wow_xhr_id: string,
    method: HttpMethod,
    url: string,
    body: string,
    headers: { [key: string]: string; },
    timeout: number,
    type: XMLHttpRequestResponseType,
    withCredentials: string
}

interface XhookResponse {
    status: number,
    statusText: string,
    text: string,
    headers: { [key: string]: string; },
    data: string,
}

interface WowXhrLog {
    request: XhookRequest,
    response: XhookResponse
}

type XhookBeforeHandler = (request: XhookRequest, callback?: () => any) => void;
type XhookAfterHandler = (request: XhookRequest, response: XhookResponse, callback?: () => any) => void;

interface XhookEvents {
    before(handler: XhookBeforeHandler): void;

    after(handler: XhookAfterHandler): void;
}

interface XhrInterceptor {
    beforeXHR(request: XhookRequest): Promise<void>;

    afterXHR(request: XhookRequest, response: XhookResponse): Promise<void>;
}

interface WowXhrMockRequest {
    headers: { [key: string]: string; },
    body: string
}

interface WowXhrMockResponse {
    headers: { [key: string]: string; },
    body: string,
    status: number,
    delay: number
}

interface WowXhrMock {
    url: string;
    method: HttpMethod,
    queryParams?: { [key: string]: string; },
    mockRequest?: WowXhrMockRequest,
    mockResponse?: WowXhrMockResponse
}

export {
    HttpMethod,

    XhookRequest,
    XhookResponse,
    XhookBeforeHandler,
    XhookAfterHandler,
    XhookEvents,
    XhrInterceptor,

    WowXhrMockRequest,
    WowXhrMockResponse,
    WowXhrLog,
    WowXhrMock
}