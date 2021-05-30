import {XhookEvents, XhookRequest, XhookResponse, XhrInterceptor} from "./types";
import {v4 as uuidv4} from 'uuid';

export function initializeXHookListener(xhook: XhookEvents, xhrInterceptors: XhrInterceptor[]) {
    xhook.before(async function (request: XhookRequest, callback: () => any) {
        request.wow_xhr_id = uuidv4();
        for (var interceptor of xhrInterceptors) {
            await interceptor.beforeXHR(request)
        }
        callback();
    })

    xhook.after(async function (request: XhookRequest, response: XhookResponse, callback: () => any) {
        for (let interceptor of xhrInterceptors) {
            await interceptor.afterXHR(request, response)
        }
        callback();
    })
}