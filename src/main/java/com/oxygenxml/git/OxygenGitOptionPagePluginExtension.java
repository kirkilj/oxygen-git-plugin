package com.oxygenxml.git;

import javax.swing.JComponent;

import ro.sync.exml.plugin.option.OptionPagePluginExtension;
import ro.sync.exml.workspace.api.PluginWorkspace;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.oxygenxml.git.options.OptionsManager;
import com.oxygenxml.git.translator.Tags;
import com.oxygenxml.git.translator.Translator;

/**
 * Plugin option page extension Repository Watcher Workspace Access Plugin Extension.
 */
public class OxygenGitOptionPagePluginExtension extends OptionPagePluginExtension{
 
  /**
   * Value for as soon as there are new commits upstream.
   */
  public static final String WARN_UPSTREAM_ALWAYS = "always";
  
  /**
   * Value for notifying when there are new commits upstream that may cause conflicts.
   */
  public static final String WARN_UPSTREAM_ON_CHANGE = "onChange";
  
  /**
   * Value for never notifying when there are new commits upstream.
   */
  public static final String WARN_UPSTREAM_NEVER = "never";
  
  /**
   * A group for buttons so that will always be only one button selected.
   */
  private ButtonGroup group;
  
  /**
   * CheckBox for the option to notify the user as soon as there are new commits.
   */
  private JCheckBox alwaysCheckBox;
  
  /**
   * CheckBox for the option to notify the user when there is a change in the opened files.
   */
  private JCheckBox onChangeCheckBox;
  
  /**
   * CheckBock for the option to never notify the user when there are new commits.
   */
  private JCheckBox neverCheckBox;
  
  /**
   * The OptionsManager instance
   */
  private static final OptionsManager optionsManager = OptionsManager.getInstance();
  
  /**
   * The Translator instance
   */
  private static final Translator translator = Translator.getInstance();
  
  /**
   * @see ro.sync.exml.plugin.option.OptionPagePluginExtension#apply(ro.sync.exml.workspace.api.PluginWorkspace)
   */
  
  @Override
  public void apply(PluginWorkspace pluginWorkspace) {
    if (alwaysCheckBox.isSelected()) {
      optionsManager.setWarnOnUpstreamChange(WARN_UPSTREAM_ALWAYS);
    } else {
      if (onChangeCheckBox.isSelected()) {
        optionsManager.setWarnOnUpstreamChange(WARN_UPSTREAM_ON_CHANGE);
      } else {
        if (neverCheckBox.isSelected()) {
          optionsManager.setWarnOnUpstreamChange(WARN_UPSTREAM_NEVER);
        }
      }
    }
    optionsManager.saveOptions();
  }

  /**
   * @see ro.sync.exml.plugin.option.OptionPagePluginExtension#restoreDefaults()
   */
  @Override
  public void restoreDefaults() {
    // Reset the text fields values. Empty string is used to map the <null> default values of the options.
    neverCheckBox.setSelected(true);
  }

  /**
   * @see ro.sync.exml.plugin.option.OptionPagePluginExtension#getTitle()
   */
  @Override
  public String getTitle() {
    return "Git Client";
  }
  
  /**
   * @see ro.sync.exml.plugin.option.OptionPagePluginExtension#init(ro.sync.exml.workspace.api.PluginWorkspace)
   */
  @Override
  public JComponent init(final PluginWorkspace pluginWorkspace) {
    GridBagConstraints c = new GridBagConstraints();
    JPanel panel = new JPanel(new GridBagLayout());
    
    //Add a label that asks us when do we want to see if there are remote changes for the repository.
    JLabel whatcherTypeLbl = new JLabel("When do you want to be notified about new commits in the remote?");
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0;
    c.weighty = 0;
    c.anchor = GridBagConstraints.LINE_START;
    panel.add(whatcherTypeLbl, c);
    
    //Create a ButtonGroup so that there will be always only one option selected.
    group = new ButtonGroup();

    //Add the alwayCheckBox option that notifies us as soon as there are any remote changes.
    c.gridy ++;
    alwaysCheckBox = new JCheckBox(translator.getTranslation(Tags.ALWAYS_NOTIFY_ON_NEW_COMMITS));
    group.add(alwaysCheckBox);
    panel.add(alwaysCheckBox, c);
    
    //Add the onChangeCheckBox option that looks for remote changes in the files opened in the editing areas.
    c.gridy ++;
    onChangeCheckBox = new JCheckBox(translator.getTranslation(Tags.NOTIFY_ON_POSSIBLE_CONFLICTS));
    group.add(onChangeCheckBox);
    panel.add(onChangeCheckBox, c);
    
    //Add the neverCheckBox option that never looks for remote changes.
    c.gridy ++;
    neverCheckBox = new JCheckBox(translator.getTranslation(Tags.NEVER_NOTIFY_ON_NEW_COMMITS));
    group.add(neverCheckBox);
    panel.add(neverCheckBox, c);
    
    c.gridx = 0;
    c.gridy ++;
    c.gridwidth = 3;
    c.weightx = 1;
    c.weighty = 1;
    c.fill = GridBagConstraints.BOTH;
    panel.add(new JPanel(), c);

    //Set the initial state of the option.
    String warnOnUpstreamChange = optionsManager.getWarnOnUpstreamChange();

    if (WARN_UPSTREAM_ALWAYS.equals(warnOnUpstreamChange)) {
      alwaysCheckBox.setSelected(true);
    } else if (WARN_UPSTREAM_ON_CHANGE.equals(warnOnUpstreamChange)) {
      onChangeCheckBox.setSelected(true);
    } else {
      neverCheckBox.setSelected(true);
    }
    
    return panel;
  }
}
