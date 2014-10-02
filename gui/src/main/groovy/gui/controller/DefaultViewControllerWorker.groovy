package gui.controller

import java.util.EventObject
import java.util.EventListener

import org.viewaframework.view.*
import org.viewaframework.controller.*

abstract class DefaultViewControllerWorker<L extends EventListener,E extends EventObject,V,R> extends
    AbstractViewControllerWorker<L,E,V,R> {

    void preHandlingView(ViewContainer view, E event) { }

    void handleView(ViewContainer view, E event) { }

    void handleViewPublising(ViewContainer view, E event, List<V> chunks) { }

    void handleViewProgress(ViewContainer view, E event, Integer progress) { }

    void postHandlingViewOnError(ViewContainer view, E event, BackgroundException ex) { }

    void setContextAttribute(String name, Object value) {
        viewManager.application.applicationContext.setAttribute(name, value)
    }

    public <U> U getContextAttribute(String name) {
        return (U) viewManager.application.applicationContext.getAttribute(name)
    }

    public <U> U getContextAttribute(String name, Object value) {
        def stored = getContextAttribute(name)
        if (stored) {
            return stored
        } else {
            setContextAttribute(name, value)
            return value
        }
    }

}
