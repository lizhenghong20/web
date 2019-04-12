package cn.farwalker.ravv.service.email;

public interface IEmailService {

    public String sendEmail(String email, String message);

    public void asynSendEmail(String email, String message);



    public void sendEmailForTest(String email, String message);

    public boolean validator(String email, String activationCode);
}
