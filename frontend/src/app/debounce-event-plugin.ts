import {Injectable} from '@angular/core';
import {EventManagerPlugin} from '@angular/platform-browser';
@Injectable()
export class DebounceEventPlugin extends EventManagerPlugin {
    constructor() {
        super(document);
    }
    // Define which events this plugin supports
    override supports(eventName: string) {
        return /debounce/.test(eventName);
    }
    // Handle the event registration
    override addEventListener(element: HTMLElement, eventName: string, handler: Function) {
        // Parse the event: e.g., "click.debounce.500"
        // event: "click", delay: 500
        const [event, method, delay = 300] = eventName.split('.');
        let timeoutId: number;
        const listener = (event: Event) => {
            clearTimeout(timeoutId);
            timeoutId = setTimeout(() => {
                handler(event);
            }, Number(delay));
        };
        element.addEventListener(event, listener);
        // Return cleanup function
        return () => {
            clearTimeout(timeoutId);
            element.removeEventListener(event, listener);
        };
    }
}
