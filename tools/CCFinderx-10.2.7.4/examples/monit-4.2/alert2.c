void handle_alert(Event_T E) {
    for(m= s->maillist; m; m= m->next) {
      if(IS_EVENT_SET(m->events, Event_get_id(E)) ||
	 /* Failed events are always reported */
	 (Event_get_id(E) == EVENT_FAILED) ) {
	Mail_T tmp;
	NEW(tmp);
	copy_mail(tmp, m);
	if(Event_get_message(E)) {
	  tmp->opt_message= xstrdup(Event_get_message(E));
	}
	substitute(&tmp, s->name, EVENT_DESCRIPTION(E));
	replace_bare_linefeed(&tmp);
	tmp->next= list;
	list= tmp;
	DEBUG("%s notification is sent to %s\n", EVENT_DESCRIPTION(E), m->to);
      }
    }
}
