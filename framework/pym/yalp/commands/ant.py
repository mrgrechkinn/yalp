import os, os.path
import shutil
import time

from yalp.utils import *

COMMANDS = ['antify']

HELP = {
    'antify': 'Create a build.xml file for this project'
}

def execute(**kargs):
    app = kargs.get("app")
    args = kargs.get("args")
    yalp_env = kargs.get("env")

    is_application = os.path.exists(os.path.join(app.path, 'conf', 'application.conf'))
    app.check()
    
    shutil.copyfile(os.path.join(yalp_env["basedir"], 'resources/build.xml'), os.path.join(app.path, 'build.xml'))
    
    print "~ OK, a build.xml file has been created"
    print "~ Define the YALP_PATH env property, and use it with ant run|start|stop"
    print "~"
