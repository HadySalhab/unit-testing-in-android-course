package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchContactItemsUseCaseTest {
    //region constants
    public static final String FILTER_ITEM = "filter_item";
    public static final String ID = "id";
    public static final String FULL_NAME = "fullName";
    public static final String FULL_PHONE_NUMBER = "fullPhoneNumber";
    public static final String IMAGE_URL = "imageUrl";
    public static final double AGE = 22d;
    //endregion constants

    //region helper fields
    @Mock
    private GetContactsHttpEndpoint mGetContactsHttpEndpoint;
    FetchContactItemsUseCase SUT;
    @Mock
    FetchContactItemsUseCase.Callbacks listener1;
    @Mock
    FetchContactItemsUseCase.Callbacks listener2;
    //endregion helper fields


    private List<ContactSchema> getListContactSchema() {
        List<ContactSchema> contactSchemasList = new ArrayList<>();
        contactSchemasList.add(new ContactSchema(ID, FULL_NAME, FULL_PHONE_NUMBER, IMAGE_URL, AGE));
        return contactSchemasList;
    }

    private List<Contact> getListContact() {
        List<Contact> contactList = new ArrayList<>();
        contactList.add(new Contact(ID, FULL_NAME, IMAGE_URL));
        return contactList;
    }


    @Before
    public void setUp() throws Exception {
        SUT = new FetchContactItemsUseCase(mGetContactsHttpEndpoint);
        success();
    }

    @Test
    public void fetchContacts_correctFilterIsPassedToEndPoint() {
        SUT.fetchContacts(FILTER_ITEM);
        verify(mGetContactsHttpEndpoint).getContacts(eq(FILTER_ITEM), any(GetContactsHttpEndpoint.Callback.class));
    }

    @Test
    public void fetchContacts_success_registeredObserversGetNotified() {
        SUT.registerListener(listener1);
        SUT.registerListener(listener2);
        //ACT, WHEN
        SUT.fetchContacts(FILTER_ITEM);
        //ASSERT, THEN
        verify(listener1).onNotifySuccess(eq(getListContact()));
        verify(listener2).onNotifySuccess(eq(getListContact()));
    }

    @Test
    public void fetchContacts_success_registeredObserversDontGetNotified() {
        SUT.registerListener(listener1);
        SUT.registerListener(listener2);
        SUT.unregister(listener2);
        //ACT, WHEN
        SUT.fetchContacts(FILTER_ITEM);
        //ASSERT, THEN
        verify(listener1).onNotifySuccess(eq(getListContact()));
        verifyNoMoreInteractions(listener2);
    }

    @Test
    public void fetchContacts_generalError_registerObserversGetgeneralError() {
        //ARRANGE, GIVEN
        generalError();
        SUT.registerListener(listener1);
        SUT.registerListener(listener2);

        //ACT, WHEN
        SUT.fetchContacts(FILTER_ITEM);
        //ASSERT, THEN
        verify(listener1).onNotifyFailure(eq(GetContactsHttpEndpoint.FailReason.GENERAL_ERROR));
        verify(listener2).onNotifyFailure(eq(GetContactsHttpEndpoint.FailReason.GENERAL_ERROR));
    }

    @Test
    public void fetchContacts_networkError_registerObserversGetNetworkError() {
        //ARRANGE, GIVEN
        networkError();
        SUT.registerListener(listener1);
        SUT.registerListener(listener2);

        //ACT, WHEN
        SUT.fetchContacts(FILTER_ITEM);
        //ASSERT, THEN
        verify(listener1).onNotifyFailure(eq(GetContactsHttpEndpoint.FailReason.NETWORK_ERROR));
        verify(listener2).onNotifyFailure(eq(GetContactsHttpEndpoint.FailReason.NETWORK_ERROR));
    }


    // region helper methods -----------------------------------------------------------------------
    private void success() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
                callback.onGetContactsSucceeded(getListContactSchema());
                return null;
            }
        }).when(mGetContactsHttpEndpoint).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
    }

    private void generalError() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
                callback.onGetContactsFailed(getGeneralErrorReason());
                return null;
            }
        }).when(mGetContactsHttpEndpoint).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
    }

    private void networkError() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
                callback.onGetContactsFailed(getNetorkError());
                return null;
            }
        }).when(mGetContactsHttpEndpoint).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
    }

    private GetContactsHttpEndpoint.FailReason getGeneralErrorReason() {
        return GetContactsHttpEndpoint.FailReason.GENERAL_ERROR;
    }
    private GetContactsHttpEndpoint.FailReason getNetorkError() {
        return GetContactsHttpEndpoint.FailReason.NETWORK_ERROR;
    }
    //endregion helper methods

    //region helper classes


    //endregion helper classes
}
