package ru.dimasokol.learning.activities;

import android.os.Message;
import android.os.SystemClock;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Дмитрий Соколов <DPSokolov.SBT@sberbank.ru>
 */

public class LoggingStorage {

    private final List<LogMessage> mLogMessages = new ArrayList<>(128);
    private WeakReference<IMessagesListener> mListener = new WeakReference<>(null);

    public void addMessage(String message) {
        mLogMessages.add(new LogMessage(message));
        fireChanges();
    }

    public void setListener(IMessagesListener listener) {
        mListener =  new WeakReference<>(listener);
        fireChanges();
    }

    public LogMessage getMessage(int index) {
        return mLogMessages.get(index);
    }

    private void fireChanges() {
        IMessagesListener listener = mListener.get();

        if (listener != null) {
            listener.onMessagesChanged(this);
        }
    }

    public int size() {
        return mLogMessages.size();
    }

    public void clear() {
        mLogMessages.clear();
        fireChanges();
    }

    public static class LogMessage {
        private final long mTimestamp;
        private final String mMessage;

        public LogMessage(String message) {
            mTimestamp = System.currentTimeMillis();
            mMessage = message;
        }

        public long getTimestamp() {
            return mTimestamp;
        }

        public String getMessage() {
            return mMessage;
        }
    }

    public interface IMessagesListener {
        void onMessagesChanged(LoggingStorage storage);
    }
}
