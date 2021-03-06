/*******************************************************************************
 * This file is part of Champions.
 *
 *     Champions is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Champions is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Champions.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.mcthepond.champs.library.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages calling and registering of events.
 *
 * @author YoshiGenius
 */
//TODO Finish making this thread safe
@SuppressWarnings("unchecked")
public class EventManager {
    private static int MAX_UPSTREAM = 10;
    private static HashMap<Method, CEventHandler> handlers = new HashMap<>();
    private static HashMap<Class<? extends ChampionsEvent>, ArrayList<Method>> methods = new HashMap<>();
    private static HashMap<Method, EventListener> listeners = new HashMap<>();

    private static EventManager instance = new EventManager();

    public static EventManager getInstance() {
        return instance;
    }

    private EventManager() {
    }

    public static synchronized void registerEvents(EventListener listener) {
        for(Method method : listener.getClass().getMethods()) {
            if(method.isAnnotationPresent(CEventHandler.class)) {
                Class[] paramType = method.getParameterTypes();
                if(paramType.length == 1 &&
                        ChampionsEvent.class.isAssignableFrom(paramType[0])) {
                    handlers.put(method, method.getAnnotation(CEventHandler.class));

                    // Register upstream events to method
                    Class clazz = paramType[0];
                    for(int i = 0; i < MAX_UPSTREAM; i++) {
                        ArrayList<Method> methodList = methods.get(clazz);
                        if(methodList == null) methodList = new ArrayList<>();
                        methodList.add(method);
                        methods.put(clazz, methodList);

                        listeners.put(method, listener);
                        clazz = clazz.getSuperclass();
                        if(clazz == Object.class) break;
                    }
                }
            }
        }
    }

    // TODO this can be optimized by mapping the priorities before the event needs to be called.
    public static synchronized void callEvent(ChampionsEvent event) {
        try {
            Class clazz = event.getClass();
            if(methods.containsKey(clazz)) {
                ArrayList<Method> lowestPriority = new ArrayList<>();
                ArrayList<Method> lowPriority = new ArrayList<>();
                ArrayList<Method> normalPriority = new ArrayList<>();
                ArrayList<Method> highPriority = new ArrayList<>();
                ArrayList<Method> highestPriority = new ArrayList<>();
                ArrayList<Method> monitorPriority = new ArrayList<>();

                // Add upstream listener methods
                for(int i = 0; i < MAX_UPSTREAM; i++) {
                    for(Method method : methods.get(clazz)) {
                        CEventHandler handler = handlers.get(method);
                        switch(handler.priority()) {
                            case LOWEST:
                                if(!lowestPriority.contains(method)) lowestPriority.add(method);
                                break;
                            case LOW:
                                if(!lowPriority.contains(method)) lowPriority.add(method);
                                break;
                            case NORMAL:
                                if(!normalPriority.contains(method)) normalPriority.add(method);
                                break;
                            case HIGH:
                                if(!highPriority.contains(method)) highPriority.add(method);
                                break;
                            case HIGHEST:
                                if(!highestPriority.contains(method)) highestPriority.add(method);
                                break;
                            case MONITOR:
                                if(!monitorPriority.contains(method)) monitorPriority.add(method);
                                break;
                        }
                    }
                    clazz = clazz.getSuperclass();
                    if(clazz == Object.class) break;
                }

                // Todo code duplication.
                for(Method method : lowestPriority) {
                    if(event instanceof Cancellable && ((Cancellable)event).isCancelled() && handlers.get(method).ignoreCancelled()) {
                        continue;
                    }
                    if (method.getParameterTypes()[0].isAssignableFrom(event.getClass())) {
                        method.invoke(listeners.get(method), event);
                    }
                }
                for(Method method : lowPriority) {
                    if(event instanceof Cancellable && ((Cancellable)event).isCancelled() && handlers.get(method).ignoreCancelled()) {
                        continue;
                    }
                    if (method.getParameterTypes()[0].isAssignableFrom(event.getClass())) {
                        method.invoke(listeners.get(method), event);
                    }
                }
                for(Method method : normalPriority) {
                    if(event instanceof Cancellable && ((Cancellable)event).isCancelled() && handlers.get(method).ignoreCancelled()) {
                        continue;
                    }
                    if (method.getParameterTypes()[0].isAssignableFrom(event.getClass())) {
                        method.invoke(listeners.get(method), event);
                    }
                }
                for(Method method : highPriority) {
                    if(event instanceof Cancellable && ((Cancellable)event).isCancelled() && handlers.get(method).ignoreCancelled()) {
                        continue;
                    }
                    if (method.getParameterTypes()[0].isAssignableFrom(event.getClass())) {
                        method.invoke(listeners.get(method), event);
                    }
                }
                for(Method method : highestPriority) {
                    if(event instanceof Cancellable && ((Cancellable)event).isCancelled() && handlers.get(method).ignoreCancelled()) {
                        continue;
                    }
                    if (method.getParameterTypes()[0].isAssignableFrom(event.getClass())) {
                        method.invoke(listeners.get(method), event);
                    }
                }
                for(Method method : monitorPriority) {
                    if(event instanceof Cancellable && ((Cancellable)event).isCancelled() && handlers.get(method).ignoreCancelled()) {
                        continue;
                    }
                    if(method.getParameterTypes()[0].isAssignableFrom(event.getClass())) {
                        method.invoke(listeners.get(method), event);
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ignored){
        }
    }

}
