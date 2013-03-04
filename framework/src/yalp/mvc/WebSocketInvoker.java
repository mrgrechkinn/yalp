package yalp.mvc;

import yalp.Yalp;
import yalp.data.validation.Validation;
import yalp.exceptions.YalpException;
import yalp.exceptions.UnexpectedException;

public class WebSocketInvoker {

    public static void resolve(Http.Request request) {
        ActionInvoker.resolve(request, null);
    }

    public static void invoke(Http.Request request, Http.Inbound inbound, Http.Outbound outbound) {

        try {

            // 1. Easy debugging ...
            if (Yalp.mode == Yalp.Mode.DEV) {
                WebSocketController.class.getDeclaredField("inbound").set(null, Http.Inbound.current());
                WebSocketController.class.getDeclaredField("outbound").set(null, Http.Outbound.current());
                WebSocketController.class.getDeclaredField("params").set(null, Scope.Params.current());
                WebSocketController.class.getDeclaredField("request").set(null, Http.Request.current());
                WebSocketController.class.getDeclaredField("session").set(null, Scope.Session.current());
                WebSocketController.class.getDeclaredField("validation").set(null, Validation.current());
            }

            ActionInvoker.invoke(request, null);

        }catch (YalpException e) {
            throw e;
        } catch (Exception e) {
            throw new UnexpectedException(e);
        }

    }
}
