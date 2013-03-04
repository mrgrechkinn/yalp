%{
    if(_delete == 'all') {
        yalp.test.Fixtures.deleteAll()
    } else if(_delete) {
        yalp.test.Fixtures.delete(_delete)
    }
}%

%{
    if(_load) {
        yalp.test.Fixtures.load(_load)
    }
}%

%{
    if(_arg && _arg instanceof String) {
        try {
            yalp.Yalp.classloader.loadClass(_arg).newInstance()
        } catch(Exception e) {
            throw new yalp.exceptions.TagInternalException('Cannot apply ' + _arg + ' fixture because of ' + e.getClass().getName() + ', ' + e.getMessage())
        }
    }
%}
