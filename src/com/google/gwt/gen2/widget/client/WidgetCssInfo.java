/*
 * Copyright 2008 Google Inc.
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

package com.google.gwt.gen2.widget.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.libideas.client.StyleInjector;
import com.google.gwt.libideas.logging.shared.Log;
import com.google.gwt.libideas.resources.client.CssResource;
import com.google.gwt.libideas.resources.client.ImmutableResourceBundle;

/**
 * Helper class to allow widgets to manage their css info.
 */
public class WidgetCssInfo {
  /**
   * CSS resources for all included widgte files.
   */
  public static interface DefaultBundle extends ImmutableResourceBundle {
    @Resource("com/google/gwt/gen2/widget/public/DropDownListBox.css")
    CssResource customListBoxCss();
  }

  static class DisabledMode extends Mode {
    @Override
    public void inject(CssResource res) {
      Log.info("default css is disabled, so not including " + res.getName());
    }

    @Override
    protected boolean shouldInject() {
      return false;
    }
  }

  static class Mode {
    public void inject(CssResource res) {
      StyleInjector.injectStylesheet(res.getText());
    }

    protected boolean shouldInject() {
      return true;
    }
  }

  public static DefaultBundle DEFAULT_CSS_FILES = GWT.create(DefaultBundle.class);

  private static Mode m = GWT.create(Mode.class);

  /**
   * If css dependency injection is enabled, adds the DropDownListBox.css file
   * included under public/widget.
   */
  public static void addDefaultDropDownListBoxFile() {
    inject(DEFAULT_CSS_FILES.customListBoxCss());
  }

  public static <CssType extends CssResource> CssType inject(CssType b) {
    m.inject(b);
    return b;
  }

  /**
   * Can any css resources be injected?
   */
  public static boolean isInjectionEnabled() {
    return m.shouldInject();
  }

  protected static void validate(Object currentBundle, Object newBundle) {
    assert currentBundle == null : "Should not initialize css info after the info has been accessed";
    assert newBundle != null : "Should never set a null resources";
  }
}
