%{
    models = [];
    for(controllerClass in yalp.Yalp.classloader.getAssignableClasses(_('controllers.CRUD'))) {
        resourceModel = _('controllers.CRUD$ObjectType').get(controllerClass)
        if(resourceModel != null) {
            models.add(resourceModel)
        }
    }
    for(controllerClass in yalp.Yalp.classloader.getAssignableClasses(_('yalp.scalasupport.crud.CRUDWrapper'))) {
        resourceModel = _('controllers.CRUD$ObjectType').get(controllerClass)
        if(resourceModel != null) {
            models.add(resourceModel)
        }
    }
    java.util.Collections.sort(models)
}%

%{ models.eachWithIndex() { item, i -> }%
	%{
		attrs = [:]
		attrs.put('type', item)
	}%
    #{doBody vars:attrs /}
%{ } }%
