package com.them.orderrelay.framework.util.mail;

import com.them.orderrelay.framework.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;

/**
 * 메일보내기
 * @param <T>
 */
@Component
public class MailInfo<T> {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    public MailInfo(){}
    public String sendMail(MailData mail) {
        return sendMail(mail,null);
    }
    public String sendMail(MailData mail, T data) {

        MailInfoThreadExec m = new MailInfoThreadExec();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                m.sendMail(mail, data, javaMailSender, templateEngine);
            }
        });

        thread.start();
        thread.setUncaughtExceptionHandler(new ThreadExceptionEmailHander(mail, data));
        return "";
    }
}


class  ThreadExceptionEmailHander implements Thread.UncaughtExceptionHandler {
    private MailData _mail;
    private Object _mailParam;
    public  ThreadExceptionEmailHander(MailData mail, Object param) {
        this._mail = mail;
        this._mailParam = param;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        System.out.println(thread.getName() + ">>> 메일보내기 오류 MailInfo => "
                + Global.getDataInfo().convertToString(_mail)
                + " || MailParam => "
                + ((_mailParam == null) ? "" :Global.getDataInfo().convertToString(_mailParam))
                + " => " + e);
        ;
    }

}

class MailInfoThreadExec{

    public MailInfoThreadExec() {}

    public String sendMail(MailData mail, JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        return this.sendMail(mail, null, javaMailSender, templateEngine);
    }

    public String sendMail(MailData mail, Object data,JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            System.out.println(">>>>>>1");

            setTitle(mail, helper);
            System.out.println(">>>>>>2");
            fromMail(mail, helper);
            toMails(mail, message);

            setContents(mail, data, templateEngine, helper);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            return e.getMessage();
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "";
    }

    private void setTitle(MailData mail, MimeMessageHelper helper) throws MessagingException {
        // 메일제목 설정
        helper.setSubject(mail.getTitle());
    }

    private void setContents(MailData mail, Object data, SpringTemplateEngine templateEngine, MimeMessageHelper helper) throws MessagingException {
        System.out.println(">>>>>>4");
        // 템플릿에 전달할 데이터
        if (Global.getDataInfo().isNullEmpty(mail.getTempleateName())) {
            mail.setTempleateName("./template/mail-template.html");
        }
        else
            mail.setTempleateName("./template/" + mail.getTempleateName());

        System.out.println(">>>>>>5");
        // message.setRecipients(Message.RecipientType., addresses);

        Context context = new Context();
        if (data == null) htmlMessageSet(mail, context);
        else htmlObjectFileSet(data, context);

        helper.setText(templateEngine.process(mail.getTempleateName(), context), true);
    }

    private void toMails(MailData mail, MimeMessage message) throws UnsupportedEncodingException, MessagingException {
        /* 받는 사람 */
        setToMails(mail, message);
        System.out.println(">>>>>>3");
        setCcMails(mail, message);
    }

    private void setToMails(MailData mail, MimeMessage message) throws UnsupportedEncodingException, MessagingException {
        String[] arrToMail = mail.getToMail().split(",");
        String[] arrToMailName = mail.getToMailName().split(",");
        InternetAddress[] toAddr = new InternetAddress[arrToMail.length];
        for (int i = 0; i < arrToMail.length; i++) {
            if (i > arrToMailName.length - 1 )
                toAddr[i] = new InternetAddress(arrToMail[i]);
            else
                toAddr[i] = new InternetAddress(arrToMail[i], arrToMailName[i]);
        }
        message.setRecipients(Message.RecipientType.TO, toAddr);
    }

    private void setCcMails(MailData mail, MimeMessage message) throws UnsupportedEncodingException, MessagingException {
        /* 참조하는 사람 */
        if (mail.getCcMail() != null) {
            String[] arrCcMail = mail.getCcMail().split(",");
            String[] arrCcMailName = mail.getCcMailName().split(",");
            InternetAddress[] ccAddr = new InternetAddress[arrCcMail.length];
            for (int i = 0; i < arrCcMail.length; i++) {
                if (i > arrCcMailName.length - 1)
                    ccAddr[i] = new InternetAddress(arrCcMail[i]);
                else
                    ccAddr[i] = new InternetAddress(arrCcMail[i], arrCcMailName[i]);
            }
            message.setRecipients(Message.RecipientType.CC, ccAddr);
        }
    }

    private void fromMail(MailData mail, MimeMessageHelper helper) throws MessagingException, UnsupportedEncodingException {
        helper.setFrom(new InternetAddress(mail.getFromMail(), mail.getFromMailName()));
    }

    private void htmlMessageSet(MailData mail, Context context) {
        context.setVariable("message", mail.getMessage());
        context.setVariable("messageType", mail.getMessageType().getName());
    }

    private void htmlObjectFileSet(Object data, Context context) {
        if(data != null) {
            LinkedHashMap<String, Object> map = Global.getDataInfo().convertClassToMap(data);
            for (String key : map.keySet()) {
                if (map.get(key) == null)
                    break;
                context.setVariable(key, map.get(key).toString());
            }
        }
    }
}