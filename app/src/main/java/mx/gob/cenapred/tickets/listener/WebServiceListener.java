package mx.gob.cenapred.tickets.listener;

import mx.gob.cenapred.tickets.entity.ResponseWebServiceEntity;

public interface WebServiceListener {
    void onCommunicationFinish(ResponseWebServiceEntity responseWebServiceEntity);
}
