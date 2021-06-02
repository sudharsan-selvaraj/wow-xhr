import {XhrLog} from "./modules/XhrLog";
import {XhrMock} from "./modules/XhrMock";
import {v4 as uuidv4} from 'uuid';
import {XhookRequest, XhookResponse} from "./types";

require("./modules/xhook.js").xhook;

var Window: any = window;

Window.xhook.enable();
Window.xhrMock = new XhrMock();
Window.xhrLog = new XhrLog();
var interceptors = [Window.xhrMock, Window.xhrLog];

Window.xhook.before(async function (request: XhookRequest, callback: () => any) {
    request.wow_xhr_id = uuidv4();
    for (var interceptor of interceptors) {
        await interceptor.beforeXHR(request)
    }
    callback();
})

Window.xhook.after(async function (request: XhookRequest, response: XhookResponse, callback: () => any) {
    for (let interceptor of interceptors) {
        await interceptor.afterXHR(request, response)
    }
    callback();
})