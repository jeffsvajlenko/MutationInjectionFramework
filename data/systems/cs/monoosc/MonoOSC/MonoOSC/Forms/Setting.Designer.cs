namespace MonoOSC.Forms
{
partial class Setting
{
    /// <summary>
    /// Variable nécessaire au concepteur.
    /// </summary>
    private System.ComponentModel.IContainer components = null;

    /// <summary>
    /// Nettoyage des ressources utilisées.
    /// </summary>
    /// <param name="disposing">true si les ressources managées doivent être supprimées ; sinon, false.</param>
    protected override void Dispose(bool disposing)
    {
        if (disposing && (components != null))
        {
            components.Dispose();
        }
        base.Dispose(disposing);
    }

    #region Code généré par le Concepteur Windows Form

    /// <summary>
    /// Méthode requise pour la prise en charge du concepteur - ne modifiez pas
    /// le contenu de cette méthode avec l'éditeur de code.
    /// </summary>
    private void InitializeComponent()
    {
        this.components = new System.ComponentModel.Container();
        this.label4 = new System.Windows.Forms.Label();
        this.label2 = new System.Windows.Forms.Label();
        this.label1 = new System.Windows.Forms.Label();
        this.TxtRpmSpecM = new System.Windows.Forms.TextBox();
        this.LblRpmSpecM = new System.Windows.Forms.Label();
        this.TxtPass = new System.Windows.Forms.TextBox();
        this.TxtUser = new System.Windows.Forms.TextBox();
        this.TxtPrefix = new System.Windows.Forms.TextBox();
        this.tableLayoutPanelOkCancel = new System.Windows.Forms.TableLayoutPanel();
        this.BtnHelp = new System.Windows.Forms.Button();
        this.BtnCancel = new System.Windows.Forms.Button();
        this.BtnOk = new System.Windows.Forms.Button();
        this.label3 = new System.Windows.Forms.Label();
        this.TxtAlternateURL = new System.Windows.Forms.TextBox();
        this.tabControl1 = new System.Windows.Forms.TabControl();
        this.tabPage1 = new System.Windows.Forms.TabPage();
        this.BtnRefreshSubPrj = new System.Windows.Forms.Button();
        this.label9 = new System.Windows.Forms.Label();
        this.CmBxSubPrj = new System.Windows.Forms.ComboBox();
        this.tabPage2 = new System.Windows.Forms.TabPage();
        this.BtnChoiceLogFs = new System.Windows.Forms.Button();
        this.TxtLogFs = new System.Windows.Forms.TextBox();
        this.label10 = new System.Windows.Forms.Label();
        this.ChkUpdate = new System.Windows.Forms.CheckBox();
        this.BtnChoiceRpmSpecManag = new System.Windows.Forms.Button();
        this.ChckConfirm = new System.Windows.Forms.CheckBox();
        this.tabPage3 = new System.Windows.Forms.TabPage();
        this.ChkProxyEnable = new System.Windows.Forms.CheckBox();
        this.TxtProxyUser = new System.Windows.Forms.TextBox();
        this.label5 = new System.Windows.Forms.Label();
        this.TxtProxyIP = new System.Windows.Forms.TextBox();
        this.TxtProxyPort = new System.Windows.Forms.TextBox();
        this.TxtProxyPass = new System.Windows.Forms.TextBox();
        this.label6 = new System.Windows.Forms.Label();
        this.label7 = new System.Windows.Forms.Label();
        this.label8 = new System.Windows.Forms.Label();
        this.tabPage4 = new System.Windows.Forms.TabPage();
        this.splitContainer1 = new System.Windows.Forms.SplitContainer();
        this.BtnChangeFont = new System.Windows.Forms.Button();
        this.LblFontChoice = new System.Windows.Forms.Label();
        this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
        this.openFileDialog1 = new System.Windows.Forms.OpenFileDialog();
        this.toolTip1 = new System.Windows.Forms.ToolTip(this.components);
        this.helpProvider1 = new System.Windows.Forms.HelpProvider();
        this.fontDialog1 = new System.Windows.Forms.FontDialog();
        this.openFileDialog2 = new System.Windows.Forms.OpenFileDialog();
        this.ChkCkLessVerb = new System.Windows.Forms.CheckBox();
        this.tableLayoutPanelOkCancel.SuspendLayout();
        this.tabControl1.SuspendLayout();
        this.tabPage1.SuspendLayout();
        this.tabPage2.SuspendLayout();
        this.tabPage3.SuspendLayout();
        this.tabPage4.SuspendLayout();
        this.splitContainer1.Panel1.SuspendLayout();
        this.splitContainer1.Panel2.SuspendLayout();
        this.splitContainer1.SuspendLayout();
        this.tableLayoutPanel1.SuspendLayout();
        this.SuspendLayout();
        //
        // label4
        //
        this.label4.AutoSize = true;
        this.label4.Location = new System.Drawing.Point(209, 42);
        this.label4.Name = "label4";
        this.label4.Size = new System.Drawing.Size(40, 13);
        this.label4.TabIndex = 22;
        this.label4.Text = "Project";
        //
        // label2
        //
        this.label2.AutoSize = true;
        this.label2.Location = new System.Drawing.Point(10, 71);
        this.label2.Name = "label2";
        this.label2.Size = new System.Drawing.Size(53, 13);
        this.label2.TabIndex = 20;
        this.label2.Text = "Password";
        //
        // label1
        //
        this.label1.AutoSize = true;
        this.label1.Location = new System.Drawing.Point(10, 42);
        this.label1.Name = "label1";
        this.label1.Size = new System.Drawing.Size(57, 13);
        this.label1.TabIndex = 19;
        this.label1.Text = "UserName";
        //
        // TxtRpmSpecM
        //
        this.TxtRpmSpecM.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                   | System.Windows.Forms.AnchorStyles.Right)));
        this.TxtRpmSpecM.Location = new System.Drawing.Point(114, 24);
        this.TxtRpmSpecM.Name = "TxtRpmSpecM";
        this.TxtRpmSpecM.Size = new System.Drawing.Size(494, 20);
        this.TxtRpmSpecM.TabIndex = 27;
        this.TxtRpmSpecM.Text = "Select the Binary full path";
        this.toolTip1.SetToolTip(this.TxtRpmSpecM, "RpmSpecManager is a like of IDE for writing spec file");
        //
        // LblRpmSpecM
        //
        this.LblRpmSpecM.AutoSize = true;
        this.LblRpmSpecM.Location = new System.Drawing.Point(9, 28);
        this.LblRpmSpecM.Name = "LblRpmSpecM";
        this.LblRpmSpecM.Size = new System.Drawing.Size(96, 13);
        this.LblRpmSpecM.TabIndex = 26;
        this.LblRpmSpecM.Text = "RpmSpecManager";
        //
        // TxtPass
        //
        this.TxtPass.Location = new System.Drawing.Point(74, 64);
        this.TxtPass.Name = "TxtPass";
        this.TxtPass.PasswordChar = '●';
        this.TxtPass.Size = new System.Drawing.Size(121, 20);
        this.TxtPass.TabIndex = 18;
        this.toolTip1.SetToolTip(this.TxtPass, "Enter your Novel login password for OBS server");
        this.TxtPass.UseSystemPasswordChar = true;
        //
        // TxtUser
        //
        this.helpProvider1.SetHelpKeyword(this.TxtUser, "UserName");
        this.helpProvider1.SetHelpNavigator(this.TxtUser, System.Windows.Forms.HelpNavigator.Find);
        this.helpProvider1.SetHelpString(this.TxtUser, "Enter your Novel login name for OBS server");
        this.TxtUser.Location = new System.Drawing.Point(74, 38);
        this.TxtUser.Name = "TxtUser";
        this.helpProvider1.SetShowHelp(this.TxtUser, true);
        this.TxtUser.Size = new System.Drawing.Size(121, 20);
        this.TxtUser.TabIndex = 17;
        this.TxtUser.Text = "surfzoid";
        this.toolTip1.SetToolTip(this.TxtUser, "Enter your Novel login name for OBS server");
        this.TxtUser.TextChanged += new System.EventHandler(this.TxtUser_TextChanged);
        //
        // TxtPrefix
        //
        this.TxtPrefix.Location = new System.Drawing.Point(288, 38);
        this.TxtPrefix.Name = "TxtPrefix";
        this.TxtPrefix.Size = new System.Drawing.Size(121, 20);
        this.TxtPrefix.TabIndex = 21;
        this.TxtPrefix.Text = "home:UserName";
        this.toolTip1.SetToolTip(this.TxtPrefix, "This will be your default and master project");
        //
        // tableLayoutPanelOkCancel
        //
        this.tableLayoutPanelOkCancel.ColumnCount = 3;
        this.tableLayoutPanelOkCancel.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 52.44755F));
        this.tableLayoutPanelOkCancel.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 47.55245F));
        this.tableLayoutPanelOkCancel.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 81F));
        this.tableLayoutPanelOkCancel.Controls.Add(this.BtnHelp, 0, 0);
        this.tableLayoutPanelOkCancel.Controls.Add(this.BtnCancel, 2, 0);
        this.tableLayoutPanelOkCancel.Controls.Add(this.BtnOk, 1, 0);
        this.tableLayoutPanelOkCancel.Dock = System.Windows.Forms.DockStyle.Fill;
        this.tableLayoutPanelOkCancel.Location = new System.Drawing.Point(3, 243);
        this.tableLayoutPanelOkCancel.Name = "tableLayoutPanelOkCancel";
        this.tableLayoutPanelOkCancel.RowCount = 1;
        this.tableLayoutPanelOkCancel.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
        this.tableLayoutPanelOkCancel.Size = new System.Drawing.Size(665, 32);
        this.tableLayoutPanelOkCancel.TabIndex = 23;
        //
        // BtnHelp
        //
        this.BtnHelp.Location = new System.Drawing.Point(3, 3);
        this.BtnHelp.Name = "BtnHelp";
        this.BtnHelp.Size = new System.Drawing.Size(61, 24);
        this.BtnHelp.TabIndex = 30;
        this.BtnHelp.Text = "Help";
        this.BtnHelp.UseVisualStyleBackColor = true;
        this.BtnHelp.Click += new System.EventHandler(this.BtnHelp_Click);
        //
        // BtnCancel
        //
        this.BtnCancel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
        this.BtnCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
        this.BtnCancel.Location = new System.Drawing.Point(601, 5);
        this.BtnCancel.Name = "BtnCancel";
        this.BtnCancel.Size = new System.Drawing.Size(61, 24);
        this.BtnCancel.TabIndex = 1;
        this.BtnCancel.Text = "Cancel";
        this.BtnCancel.UseVisualStyleBackColor = true;
        //
        // BtnOk
        //
        this.BtnOk.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
        this.BtnOk.Location = new System.Drawing.Point(519, 5);
        this.BtnOk.Name = "BtnOk";
        this.BtnOk.Size = new System.Drawing.Size(61, 24);
        this.BtnOk.TabIndex = 0;
        this.BtnOk.Text = "Ok";
        this.BtnOk.UseVisualStyleBackColor = true;
        this.BtnOk.Click += new System.EventHandler(this.BtnOk_Click);
        //
        // label3
        //
        this.label3.AutoSize = true;
        this.label3.Location = new System.Drawing.Point(209, 72);
        this.label3.Name = "label3";
        this.label3.Size = new System.Drawing.Size(74, 13);
        this.label3.TabIndex = 25;
        this.label3.Text = "Alternate URL";
        //
        // TxtAlternateURL
        //
        this.TxtAlternateURL.Location = new System.Drawing.Point(288, 68);
        this.TxtAlternateURL.Name = "TxtAlternateURL";
        this.TxtAlternateURL.Size = new System.Drawing.Size(235, 20);
        this.TxtAlternateURL.TabIndex = 24;
        this.TxtAlternateURL.Text = "https://api.opensuse.org/";
        this.toolTip1.SetToolTip(this.TxtAlternateURL, "The default openSuse API URL should be good, but you can also use a Local one");
        //
        // tabControl1
        //
        this.tabControl1.Controls.Add(this.tabPage1);
        this.tabControl1.Controls.Add(this.tabPage2);
        this.tabControl1.Controls.Add(this.tabPage3);
        this.tabControl1.Controls.Add(this.tabPage4);
        this.tabControl1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.tabControl1.Location = new System.Drawing.Point(3, 3);
        this.tabControl1.Name = "tabControl1";
        this.tabControl1.SelectedIndex = 0;
        this.tabControl1.Size = new System.Drawing.Size(665, 234);
        this.tabControl1.TabIndex = 28;
        //
        // tabPage1
        //
        this.tabPage1.Controls.Add(this.BtnRefreshSubPrj);
        this.tabPage1.Controls.Add(this.label9);
        this.tabPage1.Controls.Add(this.CmBxSubPrj);
        this.tabPage1.Controls.Add(this.TxtUser);
        this.tabPage1.Controls.Add(this.label3);
        this.tabPage1.Controls.Add(this.TxtPrefix);
        this.tabPage1.Controls.Add(this.TxtAlternateURL);
        this.tabPage1.Controls.Add(this.TxtPass);
        this.tabPage1.Controls.Add(this.label1);
        this.tabPage1.Controls.Add(this.label4);
        this.tabPage1.Controls.Add(this.label2);
        this.tabPage1.Location = new System.Drawing.Point(4, 22);
        this.tabPage1.Name = "tabPage1";
        this.tabPage1.Padding = new System.Windows.Forms.Padding(3);
        this.tabPage1.Size = new System.Drawing.Size(657, 208);
        this.tabPage1.TabIndex = 0;
        this.tabPage1.Text = "Connection";
        this.tabPage1.UseVisualStyleBackColor = true;
        //
        // BtnRefreshSubPrj
        //
        this.BtnRefreshSubPrj.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.BtnRefreshSubPrj.BackgroundImage = global::MonoOSC.Properties.Resources.Refresh;
        this.BtnRefreshSubPrj.Location = new System.Drawing.Point(626, 37);
        this.BtnRefreshSubPrj.Name = "BtnRefreshSubPrj";
        this.BtnRefreshSubPrj.Size = new System.Drawing.Size(24, 23);
        this.BtnRefreshSubPrj.TabIndex = 29;
        this.toolTip1.SetToolTip(this.BtnRefreshSubPrj, "Refresh sub project list");
        this.BtnRefreshSubPrj.UseVisualStyleBackColor = true;
        this.BtnRefreshSubPrj.Click += new System.EventHandler(this.BtnRefreshSubPrj_Click);
        //
        // label9
        //
        this.label9.AutoSize = true;
        this.label9.Location = new System.Drawing.Point(415, 42);
        this.label9.Name = "label9";
        this.label9.Size = new System.Drawing.Size(58, 13);
        this.label9.TabIndex = 28;
        this.label9.Text = "Subproject";
        //
        // CmBxSubPrj
        //
        this.CmBxSubPrj.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                  | System.Windows.Forms.AnchorStyles.Right)));
        this.CmBxSubPrj.FormattingEnabled = true;
        this.CmBxSubPrj.Location = new System.Drawing.Point(479, 38);
        this.CmBxSubPrj.Name = "CmBxSubPrj";
        this.CmBxSubPrj.Size = new System.Drawing.Size(141, 21);
        this.CmBxSubPrj.TabIndex = 27;
        this.toolTip1.SetToolTip(this.CmBxSubPrj, "All subproject will come here after a refresh the first time or when changed your" +
                                 " main project");
        //
        // tabPage2
        //
        this.tabPage2.Controls.Add(this.ChkCkLessVerb);
        this.tabPage2.Controls.Add(this.BtnChoiceLogFs);
        this.tabPage2.Controls.Add(this.TxtLogFs);
        this.tabPage2.Controls.Add(this.label10);
        this.tabPage2.Controls.Add(this.ChkUpdate);
        this.tabPage2.Controls.Add(this.BtnChoiceRpmSpecManag);
        this.tabPage2.Controls.Add(this.ChckConfirm);
        this.tabPage2.Controls.Add(this.TxtRpmSpecM);
        this.tabPage2.Controls.Add(this.LblRpmSpecM);
        this.tabPage2.Location = new System.Drawing.Point(4, 22);
        this.tabPage2.Name = "tabPage2";
        this.tabPage2.Padding = new System.Windows.Forms.Padding(3);
        this.tabPage2.Size = new System.Drawing.Size(657, 208);
        this.tabPage2.TabIndex = 1;
        this.tabPage2.Text = "Misc";
        this.tabPage2.UseVisualStyleBackColor = true;
        //
        // BtnChoiceLogFs
        //
        this.BtnChoiceLogFs.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.BtnChoiceLogFs.Location = new System.Drawing.Point(614, 135);
        this.BtnChoiceLogFs.Name = "BtnChoiceLogFs";
        this.BtnChoiceLogFs.Size = new System.Drawing.Size(37, 20);
        this.BtnChoiceLogFs.TabIndex = 33;
        this.BtnChoiceLogFs.Text = "●●●";
        this.BtnChoiceLogFs.UseVisualStyleBackColor = true;
        this.BtnChoiceLogFs.Click += new System.EventHandler(this.BtnChoiceLogFs_Click);
        //
        // TxtLogFs
        //
        this.TxtLogFs.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                | System.Windows.Forms.AnchorStyles.Right)));
        this.TxtLogFs.Location = new System.Drawing.Point(114, 136);
        this.TxtLogFs.Name = "TxtLogFs";
        this.TxtLogFs.Size = new System.Drawing.Size(494, 20);
        this.TxtLogFs.TabIndex = 32;
        this.toolTip1.SetToolTip(this.TxtLogFs, "RpmSpecManager is a like of IDE for writing spec file");
        //
        // label10
        //
        this.label10.AutoSize = true;
        this.label10.Location = new System.Drawing.Point(64, 140);
        this.label10.Name = "label10";
        this.label10.Size = new System.Drawing.Size(41, 13);
        this.label10.TabIndex = 31;
        this.label10.Text = "Log file";
        //
        // ChkUpdate
        //
        this.ChkUpdate.AutoSize = true;
        this.ChkUpdate.Checked = true;
        this.ChkUpdate.CheckState = System.Windows.Forms.CheckState.Checked;
        this.ChkUpdate.Location = new System.Drawing.Point(114, 99);
        this.ChkUpdate.Name = "ChkUpdate";
        this.ChkUpdate.Size = new System.Drawing.Size(140, 17);
        this.ChkUpdate.TabIndex = 30;
        this.ChkUpdate.Text = "Check update at startup";
        this.toolTip1.SetToolTip(this.ChkUpdate, "Check if a new version of MonoOSC is aviable");
        this.ChkUpdate.UseVisualStyleBackColor = true;
        //
        // BtnChoiceRpmSpecManag
        //
        this.BtnChoiceRpmSpecManag.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.BtnChoiceRpmSpecManag.Location = new System.Drawing.Point(614, 23);
        this.BtnChoiceRpmSpecManag.Name = "BtnChoiceRpmSpecManag";
        this.BtnChoiceRpmSpecManag.Size = new System.Drawing.Size(37, 20);
        this.BtnChoiceRpmSpecManag.TabIndex = 29;
        this.BtnChoiceRpmSpecManag.Text = "●●●";
        this.BtnChoiceRpmSpecManag.UseVisualStyleBackColor = true;
        this.BtnChoiceRpmSpecManag.Click += new System.EventHandler(this.BtnChoiceRpmSpecManag_Click);
        //
        // ChckConfirm
        //
        this.ChckConfirm.AutoSize = true;
        this.ChckConfirm.Checked = true;
        this.ChckConfirm.CheckState = System.Windows.Forms.CheckState.Checked;
        this.ChckConfirm.Location = new System.Drawing.Point(114, 66);
        this.ChckConfirm.Name = "ChckConfirm";
        this.ChckConfirm.Size = new System.Drawing.Size(161, 17);
        this.ChckConfirm.TabIndex = 28;
        this.ChckConfirm.Text = "Confirm (warn) at connection";
        this.toolTip1.SetToolTip(this.ChckConfirm, "Will show you a messagebox each time you connect");
        this.ChckConfirm.UseVisualStyleBackColor = true;
        //
        // tabPage3
        //
        this.tabPage3.Controls.Add(this.ChkProxyEnable);
        this.tabPage3.Controls.Add(this.TxtProxyUser);
        this.tabPage3.Controls.Add(this.label5);
        this.tabPage3.Controls.Add(this.TxtProxyIP);
        this.tabPage3.Controls.Add(this.TxtProxyPort);
        this.tabPage3.Controls.Add(this.TxtProxyPass);
        this.tabPage3.Controls.Add(this.label6);
        this.tabPage3.Controls.Add(this.label7);
        this.tabPage3.Controls.Add(this.label8);
        this.tabPage3.Location = new System.Drawing.Point(4, 22);
        this.tabPage3.Name = "tabPage3";
        this.tabPage3.Padding = new System.Windows.Forms.Padding(3);
        this.tabPage3.Size = new System.Drawing.Size(657, 208);
        this.tabPage3.TabIndex = 2;
        this.tabPage3.Text = "Proxy";
        this.tabPage3.UseVisualStyleBackColor = true;
        //
        // ChkProxyEnable
        //
        this.ChkProxyEnable.AutoSize = true;
        this.ChkProxyEnable.Location = new System.Drawing.Point(32, 11);
        this.ChkProxyEnable.Name = "ChkProxyEnable";
        this.ChkProxyEnable.Size = new System.Drawing.Size(59, 17);
        this.ChkProxyEnable.TabIndex = 34;
        this.ChkProxyEnable.Text = "Enable";
        this.ChkProxyEnable.UseVisualStyleBackColor = true;
        this.ChkProxyEnable.CheckedChanged += new System.EventHandler(this.ChkProxyEnable_CheckedChanged);
        //
        // TxtProxyUser
        //
        this.TxtProxyUser.Location = new System.Drawing.Point(93, 34);
        this.TxtProxyUser.Name = "TxtProxyUser";
        this.TxtProxyUser.Size = new System.Drawing.Size(121, 20);
        this.TxtProxyUser.TabIndex = 26;
        this.TxtProxyUser.Text = "surfzoid";
        //
        // label5
        //
        this.label5.AutoSize = true;
        this.label5.Location = new System.Drawing.Point(253, 68);
        this.label5.Name = "label5";
        this.label5.Size = new System.Drawing.Size(26, 13);
        this.label5.TabIndex = 33;
        this.label5.Text = "Port";
        //
        // TxtProxyIP
        //
        this.TxtProxyIP.Location = new System.Drawing.Point(328, 34);
        this.TxtProxyIP.Name = "TxtProxyIP";
        this.TxtProxyIP.Size = new System.Drawing.Size(235, 20);
        this.TxtProxyIP.TabIndex = 30;
        this.TxtProxyIP.Text = "127.0.0.1";
        //
        // TxtProxyPort
        //
        this.TxtProxyPort.Location = new System.Drawing.Point(328, 64);
        this.TxtProxyPort.Name = "TxtProxyPort";
        this.TxtProxyPort.Size = new System.Drawing.Size(101, 20);
        this.TxtProxyPort.TabIndex = 32;
        this.TxtProxyPort.Text = "8080";
        //
        // TxtProxyPass
        //
        this.TxtProxyPass.Location = new System.Drawing.Point(93, 60);
        this.TxtProxyPass.Name = "TxtProxyPass";
        this.TxtProxyPass.PasswordChar = '●';
        this.TxtProxyPass.Size = new System.Drawing.Size(121, 20);
        this.TxtProxyPass.TabIndex = 27;
        this.TxtProxyPass.UseSystemPasswordChar = true;
        //
        // label6
        //
        this.label6.AutoSize = true;
        this.label6.Location = new System.Drawing.Point(29, 38);
        this.label6.Name = "label6";
        this.label6.Size = new System.Drawing.Size(57, 13);
        this.label6.TabIndex = 28;
        this.label6.Text = "UserName";
        //
        // label7
        //
        this.label7.AutoSize = true;
        this.label7.Location = new System.Drawing.Point(253, 38);
        this.label7.Name = "label7";
        this.label7.Size = new System.Drawing.Size(54, 13);
        this.label7.TabIndex = 31;
        this.label7.Text = "Adress/IP";
        //
        // label8
        //
        this.label8.AutoSize = true;
        this.label8.Location = new System.Drawing.Point(29, 67);
        this.label8.Name = "label8";
        this.label8.Size = new System.Drawing.Size(53, 13);
        this.label8.TabIndex = 29;
        this.label8.Text = "Password";
        //
        // tabPage4
        //
        this.tabPage4.Controls.Add(this.splitContainer1);
        this.tabPage4.Location = new System.Drawing.Point(4, 22);
        this.tabPage4.Name = "tabPage4";
        this.tabPage4.Padding = new System.Windows.Forms.Padding(3);
        this.tabPage4.Size = new System.Drawing.Size(657, 208);
        this.tabPage4.TabIndex = 3;
        this.tabPage4.Text = "Skin";
        this.tabPage4.UseVisualStyleBackColor = true;
        //
        // splitContainer1
        //
        this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.splitContainer1.FixedPanel = System.Windows.Forms.FixedPanel.Panel1;
        this.splitContainer1.Location = new System.Drawing.Point(3, 3);
        this.splitContainer1.Name = "splitContainer1";
        this.splitContainer1.Orientation = System.Windows.Forms.Orientation.Horizontal;
        //
        // splitContainer1.Panel1
        //
        this.splitContainer1.Panel1.Controls.Add(this.BtnChangeFont);
        //
        // splitContainer1.Panel2
        //
        this.splitContainer1.Panel2.Controls.Add(this.LblFontChoice);
        this.splitContainer1.Size = new System.Drawing.Size(651, 202);
        this.splitContainer1.SplitterDistance = 35;
        this.splitContainer1.TabIndex = 2;
        //
        // BtnChangeFont
        //
        this.BtnChangeFont.Enabled = false;
        this.BtnChangeFont.Location = new System.Drawing.Point(3, 3);
        this.BtnChangeFont.Name = "BtnChangeFont";
        this.BtnChangeFont.Size = new System.Drawing.Size(60, 30);
        this.BtnChangeFont.TabIndex = 1;
        this.BtnChangeFont.Text = "Change";
        this.BtnChangeFont.UseVisualStyleBackColor = true;
        this.BtnChangeFont.Click += new System.EventHandler(this.BtnChangeFont_Click);
        //
        // LblFontChoice
        //
        this.LblFontChoice.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
        this.LblFontChoice.Dock = System.Windows.Forms.DockStyle.Fill;
        this.LblFontChoice.Location = new System.Drawing.Point(0, 0);
        this.LblFontChoice.Name = "LblFontChoice";
        this.LblFontChoice.Size = new System.Drawing.Size(651, 163);
        this.LblFontChoice.TabIndex = 0;
        this.LblFontChoice.Text = "Font";
        this.LblFontChoice.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
        //
        // tableLayoutPanel1
        //
        this.tableLayoutPanel1.ColumnCount = 1;
        this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
        this.tableLayoutPanel1.Controls.Add(this.tableLayoutPanelOkCancel, 0, 1);
        this.tableLayoutPanel1.Controls.Add(this.tabControl1, 0, 0);
        this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
        this.tableLayoutPanel1.Name = "tableLayoutPanel1";
        this.tableLayoutPanel1.RowCount = 2;
        this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
        this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 38F));
        this.tableLayoutPanel1.Size = new System.Drawing.Size(671, 278);
        this.tableLayoutPanel1.TabIndex = 29;
        //
        // openFileDialog1
        //
        this.openFileDialog1.Filter = "All files|*.*|Win32 binary|*.exe";
        this.openFileDialog1.ReadOnlyChecked = true;
        this.openFileDialog1.RestoreDirectory = true;
        this.openFileDialog1.ShowHelp = true;
        this.openFileDialog1.ShowReadOnly = true;
        this.openFileDialog1.SupportMultiDottedExtensions = true;
        this.openFileDialog1.Title = "Select the RpmSpecManager binary";
        //
        // helpProvider1
        //
        this.helpProvider1.HelpNamespace = "http://en.opensuse.org/MonoOSC/Guide/Start";
        //
        // openFileDialog2
        //
        this.openFileDialog2.Filter = "All files|*.*|Text files|*.txt";
        this.openFileDialog2.ReadOnlyChecked = true;
        this.openFileDialog2.RestoreDirectory = true;
        this.openFileDialog2.ShowHelp = true;
        this.openFileDialog2.ShowReadOnly = true;
        this.openFileDialog2.SupportMultiDottedExtensions = true;
        this.openFileDialog2.Title = "Select a file where to store all log entries";
        //
        // ChkCkLessVerb
        //
        this.ChkCkLessVerb.AutoSize = true;
        this.ChkCkLessVerb.Checked = true;
        this.ChkCkLessVerb.CheckState = System.Windows.Forms.CheckState.Checked;
        this.ChkCkLessVerb.Location = new System.Drawing.Point(326, 66);
        this.ChkCkLessVerb.Name = "ChkCkLessVerb";
        this.ChkCkLessVerb.Size = new System.Drawing.Size(185, 17);
        this.ChkCkLessVerb.TabIndex = 34;
        this.ChkCkLessVerb.Text = "Less verbose in the console(perf!)";
        this.toolTip1.SetToolTip(this.ChkCkLessVerb, "Less verbose in the console should increase performances");
        this.ChkCkLessVerb.UseVisualStyleBackColor = true;
        //
        // Setting
        //
        this.AcceptButton = this.BtnOk;
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.CancelButton = this.BtnCancel;
        this.ClientSize = new System.Drawing.Size(671, 278);
        this.Controls.Add(this.tableLayoutPanel1);
        this.HelpButton = true;
        this.helpProvider1.SetHelpKeyword(this, "Parameter");
        this.helpProvider1.SetHelpNavigator(this, System.Windows.Forms.HelpNavigator.Find);
        this.helpProvider1.SetHelpString(this, "Set all parameter of MonoOSC");
        this.Name = "Setting";
        this.helpProvider1.SetShowHelp(this, true);
        this.Text = "Parameter";
        this.Load += new System.EventHandler(this.Setting_Load);
        this.Shown += new System.EventHandler(this.Setting_Shown);
        this.tableLayoutPanelOkCancel.ResumeLayout(false);
        this.tabControl1.ResumeLayout(false);
        this.tabPage1.ResumeLayout(false);
        this.tabPage1.PerformLayout();
        this.tabPage2.ResumeLayout(false);
        this.tabPage2.PerformLayout();
        this.tabPage3.ResumeLayout(false);
        this.tabPage3.PerformLayout();
        this.tabPage4.ResumeLayout(false);
        this.splitContainer1.Panel1.ResumeLayout(false);
        this.splitContainer1.Panel2.ResumeLayout(false);
        this.splitContainer1.ResumeLayout(false);
        this.tableLayoutPanel1.ResumeLayout(false);
        this.ResumeLayout(false);

    }

    #endregion

    private System.Windows.Forms.Label label4;
    private System.Windows.Forms.Label label2;
    private System.Windows.Forms.Label label1;
    private System.Windows.Forms.TextBox TxtRpmSpecM;
    private System.Windows.Forms.Label LblRpmSpecM;
    private System.Windows.Forms.TextBox TxtPass;
    private System.Windows.Forms.TextBox TxtUser;
    private System.Windows.Forms.TextBox TxtPrefix;
    private System.Windows.Forms.TableLayoutPanel tableLayoutPanelOkCancel;
    private System.Windows.Forms.Button BtnCancel;
    private System.Windows.Forms.Button BtnOk;
    private System.Windows.Forms.Label label3;
    private System.Windows.Forms.TextBox TxtAlternateURL;
    private System.Windows.Forms.TabControl tabControl1;
    private System.Windows.Forms.TabPage tabPage1;
    private System.Windows.Forms.TabPage tabPage2;
    private System.Windows.Forms.CheckBox ChckConfirm;
    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
    private System.Windows.Forms.Button BtnChoiceRpmSpecManag;
    private System.Windows.Forms.OpenFileDialog openFileDialog1;
    private System.Windows.Forms.TabPage tabPage3;
    private System.Windows.Forms.TextBox TxtProxyUser;
    private System.Windows.Forms.Label label5;
    private System.Windows.Forms.TextBox TxtProxyIP;
    private System.Windows.Forms.TextBox TxtProxyPort;
    private System.Windows.Forms.TextBox TxtProxyPass;
    private System.Windows.Forms.Label label6;
    private System.Windows.Forms.Label label7;
    private System.Windows.Forms.Label label8;
    private System.Windows.Forms.CheckBox ChkProxyEnable;
    private System.Windows.Forms.CheckBox ChkUpdate;
    private System.Windows.Forms.Label label9;
    private System.Windows.Forms.ComboBox CmBxSubPrj;
    private System.Windows.Forms.Button BtnRefreshSubPrj;
    private System.Windows.Forms.ToolTip toolTip1;
    private System.Windows.Forms.HelpProvider helpProvider1;
    private System.Windows.Forms.TabPage tabPage4;
    private System.Windows.Forms.Button BtnChangeFont;
    private System.Windows.Forms.Label LblFontChoice;
    private System.Windows.Forms.FontDialog fontDialog1;
    private System.Windows.Forms.SplitContainer splitContainer1;
    private System.Windows.Forms.Button BtnChoiceLogFs;
    private System.Windows.Forms.TextBox TxtLogFs;
    private System.Windows.Forms.Label label10;
    private System.Windows.Forms.OpenFileDialog openFileDialog2;
    private System.Windows.Forms.Button BtnHelp;
    private System.Windows.Forms.CheckBox ChkCkLessVerb;
}
}
