package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

public class FetchContactItemsUseCase {
    private List<Callbacks> listeners = new ArrayList<>();
    private GetContactsHttpEndpoint mGetContactsHttpEndpoint;

    public FetchContactItemsUseCase(GetContactsHttpEndpoint getContactsHttpEndpoint) {
        mGetContactsHttpEndpoint = getContactsHttpEndpoint;
    }

    public void fetchContacts(String filterItem) {
        mGetContactsHttpEndpoint.getContacts(filterItem, new GetContactsHttpEndpoint.Callback() {
            @Override
            public void onGetContactsSucceeded(List<ContactSchema> contactSchemas) {
                List<Contact> contacts = new ArrayList<>(contactSchemas.size());
                for (ContactSchema contactSchema:contactSchemas){
                    contacts.add(new Contact(contactSchema.getId(),contactSchema.getFullName(),contactSchema.getImageUrl()));
                }
                for (Callbacks listener:listeners){
                    listener.onNotifySuccess(contacts);
                }
            }

            @Override
            public void onGetContactsFailed(GetContactsHttpEndpoint.FailReason failReason) {
                for (Callbacks listener:listeners){
                    listener.onNotifyFailure(failReason);
                }
            }
        });
    }

    public void registerListener(Callbacks listener1) {
        listeners.add(listener1);
    }

    public void unregister(Callbacks listener2) {
        listeners.remove(listener2);
    }

    public interface Callbacks {
        void onNotifySuccess(List<Contact> contacts);

        void onNotifyFailure(GetContactsHttpEndpoint.FailReason failReason);
    }
}

