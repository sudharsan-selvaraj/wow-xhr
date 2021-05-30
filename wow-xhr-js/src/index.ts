import {XhrLog} from "./modules/XhrLog";

require("xhook");
import {XhrMock} from "./modules/XhrMock";
import {XhookEvents} from "./types";
import {initializeXHookListener} from "./XhookListener";
import {XhrWait} from "./modules/XhrWait";


const xhook: XhookEvents = (window as any).xhook;

const xhrMockObj: XhrMock = new XhrMock();
const xhrLogObj: XhrLog = new XhrLog();
const xhrWaitObj: XhrWait = new XhrWait(xhrLogObj);

initializeXHookListener(xhook, [xhrMockObj, xhrLogObj]);

(window as any).wowXhr = {
    mock: xhrMockObj,
    log: xhrLogObj,
    wait: xhrWaitObj
}
