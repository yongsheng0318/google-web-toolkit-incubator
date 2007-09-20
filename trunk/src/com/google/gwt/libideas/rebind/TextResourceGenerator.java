/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.libideas.rebind;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.dev.util.Util;
import com.google.gwt.libideas.client.TextResource;
import com.google.gwt.user.rebind.SourceWriter;

import java.net.URL;

/**
 * Provides implementations of TextResource.
 * 
 */
public class TextResourceGenerator implements ResourceGenerator {
  ResourceContext context;

  public void finish() {
    // no-op
  }

  public void init(ResourceContext context) {
    this.context = context;
  }

  public void writeAssignment(JMethod method) throws UnableToCompleteException {
    TreeLogger logger = context.getLogger();
    URL[] resources = ResourceGeneratorUtil.findResources(context, method);

    if (resources.length != 1) {
      logger.log(TreeLogger.ERROR, "Exactly one "
          + ResourceGeneratorUtil.METADATA_TAG + " must be specified", null);
      throw new UnableToCompleteException();
    }

    URL resource = resources[0];

    SourceWriter sw = context.getSourceWriter();
    // Write the expression to create the subtype.
    sw.println("new " + TextResource.class.getName() + "() {");
    sw.indent();

    // Convenience when examining the generated code.
    sw.println("// " + resource.toExternalForm());

    sw.println("public String getText() {");
    sw.indent();
    sw.println("return \""
        + Generator.escape(new String(Util.readURLAsChars(resource)))
            .replaceAll("\0", "\\0") + "\";");
    sw.outdent();
    sw.println("}");

    sw.println("public String getName() {");
    sw.indent();
    sw.println("return \"" + method.getName() + "\";");
    sw.outdent();
    sw.println("}");

    sw.outdent();
    sw.println("}");
  }

}
