package br.edu.ifpb.pweb1.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("dateTimeConverter")
public class DateTimeConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String date) {
		if (date.isEmpty())
			return null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
		LocalDateTime localDate = LocalDateTime.parse(date,formatter);		
		return localDate;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value == null)
			return "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
		String data = ((LocalDateTime) value).format(formatter);
		return data;
	}

}
