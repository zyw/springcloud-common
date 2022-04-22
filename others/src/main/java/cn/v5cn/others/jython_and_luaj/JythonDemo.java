package cn.v5cn.others.jython_and_luaj;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class JythonDemo {
    public static void main(String[] args) {
        try(PythonInterpreter pyInterp = new PythonInterpreter()) {
            //pyInterp.exec("print('Hello Python World!')");
//            pyInterp.exec("import subprocess");
//            pyInterp.exec("subprocess.getstatusoutput('ls')");

            pyInterp.execfile("E:\\PycharmProjects\\tornado-demo\\test.py");

            PyObject result = pyInterp.get("result");

            System.out.println(result);

        }
    }
}
