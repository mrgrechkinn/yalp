import os, os.path
import shutil
import urllib, urllib2
import subprocess
import simplejson as json

from yalp.utils import *

COMMANDS = ['dependencies','deps']

HELP = {
    'dependencies': 'Resolve and retrieve project dependencies'
}

def execute(**kargs):
    args = kargs.get("args")
    yalp_env = kargs.get("env")

    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    yalp_env = kargs.get("env")

    force = "false"
    trim = "false"
    if args.count('--forceCopy') == 1:
        args.remove('--forceCopy')
        force = "true"
        
    if args.count('--forProd') == 1:
        args.remove('--forProd')
        force = "true"
        trim = "true"

    classpath = app.getClasspath()

    add_options = ['-Dapplication.path=%s' % (app.path), '-Dframework.path=%s' % (yalp_env['basedir']), '-Dyalp.id=%s' % yalp_env['id'], '-Dyalp.version=%s' % yalp_env['version'], '-Dyalp.forcedeps=%s' % (force), '-Dyalp.trimdeps=%s' % (trim)]
    if args.count('--verbose'):
        add_options.append('-Dverbose')
    if args.count('--sync'):
        add_options.append('-Dsync')
    if args.count('--debug'):
        add_options.append('-Ddebug')
    if args.count('--clearcache'):
        add_options.append('-Dclearcache')
    if args.count('--jpda'):
        print "~ Waiting for JPDA client to continue"
        add_options.extend(['-Xdebug', '-Xrunjdwp:transport=dt_socket,address=8888,server=y,suspend=y'])
    for arg in args:
        if arg.startswith("-D"):
            add_options.append(arg)

    java_cmd = [app.java_path()] + add_options + ['-classpath', app.fw_cp_args(), 'yalp.deps.DependenciesManager']

    return_code = subprocess.call(java_cmd, env=os.environ)
    if 0 != return_code:
        sys.exit(return_code);

