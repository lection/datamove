/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util.saver;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public interface SaverHandler {
    void execute(File sourceFile,File targetFile,String template) throws IOException;
}
