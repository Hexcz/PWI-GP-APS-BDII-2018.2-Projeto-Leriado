package br.edu.ifpb.pweb1.listener;


import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

import br.edu.ifpb.pweb1.controller.LoginMB;

public class AuthListener implements PhaseListener{
	
	private static String[] paginasPublicas = {"index.xhtml","cadastro-sucesso.xhtml"};
	
	private boolean isPublica(String paginaAtual) {
		for (String pagina : paginasPublicas) {			
			if(paginaAtual.indexOf(pagina)>-1) {
				return true;
			}
		}
		return false;		
	}
	
	private void irPara(String destino) {				 
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, destino);
	}
		
	@Override
	public void afterPhase(PhaseEvent event) {
		boolean isRestrito;
		boolean isLogado;
		String viewId = event.getFacesContext().getViewRoot().getViewId();
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
				
		if(request.getSession() == null) {			
			irPara("goLogin");
		}	
		
		LoginMB loginMb = (LoginMB)request.getSession(false).getAttribute("loginBean");
			
		isRestrito = !isPublica(viewId);
		
		isLogado = ((loginMb != null) && (loginMb.getUsuarioLogado() != null));
		
		//NÃO ESTÁ LOGADO EM PÁGINA RESTRITA
		if (!isLogado && isRestrito){			
			irPara("goLogin");
		}
		//ESTÁ LOGADO EM PÁGINA PÚBLICA
		if (isLogado && !isRestrito){			
			irPara("goHome");
		}			
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
