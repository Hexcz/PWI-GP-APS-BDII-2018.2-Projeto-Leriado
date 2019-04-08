package br.edu.ifpb.pweb1.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import br.edu.ifpb.pweb1.model.dao.impdb.MensagemDaoImpl;
import br.edu.ifpb.pweb1.model.domain.Mensagem;



@ManagedBean
@RequestScoped
public class MensagemBean {
	private MensagemDaoImpl mensagemDao;
	private Mensagem msg;
	private List<Mensagem> msgs;
//	private static String idConversa="";
	
	@ManagedProperty("#{loginBean}")
	private LoginMB loginBean;
	
	@PostConstruct
	public void init() {
		mensagemDao = new MensagemDaoImpl();
		msgs = new ArrayList<>();
		msg = new Mensagem();
		
	}
	
	public String criarMensagem() {
//		msg.setIdConversa(idConversa);
		msg.setIdRemetente(""+loginBean.getUsuarioLogado().getId());
		msg.setNomeRemetente(loginBean.getUsuarioLogado().getNome());
		mensagemDao.criar(msg);
		return "";
	}
	
	public void listarMensagens() {
		msgs = mensagemDao.recuperarMensagens(msg.getIdDestinatario(), ""+loginBean.getUsuarioLogado().getId());
	}

	public List<Mensagem> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<Mensagem> msgs) {
		this.msgs = msgs;
	}

	public Mensagem getMsg() {
		return msg;
	}

	public void setMsg(Mensagem msg) {
		this.msg = msg;
	}

	public LoginMB getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginMB loginBean) {
		this.loginBean = loginBean;
	}
	
	
	
}
