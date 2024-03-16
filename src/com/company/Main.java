package com.company;

public class Main {

    public static void main(String[] args) {
	    // Паттерн (бизнес делегат) - понадобиться при кешировании данных
        BusinessDelega businessDelega = new BusinessDelega();
        businessDelega.doTask("jms");
    }
}

// у нас есть 2 сервиса
interface BusinessService{
    void doJob();
}

class EJBService implements  BusinessService{
    @Override
    public void doJob() {
        System.out.println("do job EJB");
    }
}

class JMSService implements  BusinessService{
    @Override
    public void doJob() {
        System.out.println("do job JMS");
    }
}

// но что-бы ими пользоваться нужен промежуточный объект,
// который будет выяснять какой сервис запустить
// здесь можно закешировать данные, что-бы не отнимать время серисов(серверов или вычислительных устройств)
class LookupService{
    BusinessService getService(String typeService){
        switch (typeService){
            case "ejb": return new EJBService();
            case "jms": return new JMSService();
            default: return null;
        }
    }
}

// делегирует наши сервисы, что-бы вызывать нена прямую, т.к. в будущем их будем кешировать
class BusinessDelega {
    LookupService lookupService = new LookupService();
    BusinessService businessService;

    void doTask(String typeService){
        businessService = lookupService.getService(typeService);
        businessService.doJob();
    }
}