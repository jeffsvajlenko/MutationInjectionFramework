protected void populateShell()
    {
        shell.setText(Labels.getLabel("title.configDetect"));
        shell.setLayout(LayoutHelper.formLayout(10, 10, 10));

        Label infoLabel = new Label(shell, SWT.WRAP);
        infoLabel.setText(Labels.getLabel("text.configDetect"));
        infoLabel.setLayoutData(LayoutHelper.formData(340, SWT.DEFAULT, new FormAttachment(0), new FormAttachment(100), new FormAttachment(0), null));

        Label hostLabel = new Label(shell, SWT.WRAP);
        hostLabel.setText(Labels.getLabel("text.configDetect.host"));
        hostLabel.setLayoutData(LayoutHelper.formData(new FormAttachment(0), null, new FormAttachment(infoLabel), null));
        hostText = new Text(shell, SWT.BORDER);
        hostText.setText("www");
        hostText.setLayoutData(LayoutHelper.formData(100, SWT.DEFAULT, new FormAttachment(hostLabel), null, new FormAttachment(hostLabel, 0, SWT.CENTER), null));
        Label portLabel = new Label(shell, SWT.WRAP);
        portLabel.setText(Labels.getLabel("text.configDetect.port"));
        portLabel.setLayoutData(LayoutHelper.formData(new FormAttachment(hostText, 10), null, new FormAttachment(infoLabel), null));
        portText = new Text(shell, SWT.BORDER);
        portText.setText("80");
        portText.setLayoutData(LayoutHelper.formData(30, SWT.DEFAULT, new FormAttachment(portLabel), null, new FormAttachment(portLabel, 0, SWT.CENTER), null));

        Label tryLabel = new Label(shell, SWT.NONE);
        tryLabel.setText(Labels.getLabel("text.configDetect.tries"));
        tryLabel.setLayoutData(LayoutHelper.formData(new FormAttachment(0), null, new FormAttachment(hostLabel, 10), null));
        tryCountLabel = new Label(shell, SWT.NONE);
        tryCountLabel.setLayoutData(LayoutHelper.formData(new FormAttachment(tryLabel, -5), new FormAttachment(100), new FormAttachment(hostLabel, 10), null));
        tryProgressBar = new ProgressBar(shell, SWT.NONE);
        tryProgressBar.setLayoutData(LayoutHelper.formData(new FormAttachment(0), new FormAttachment(100), new FormAttachment(tryLabel), null));

        Label successLabel = new Label(shell, SWT.NONE);
        successLabel.setText(Labels.getLabel("text.configDetect.successes"));
        successLabel.setLayoutData(LayoutHelper.formData(new FormAttachment(0), null, new FormAttachment(tryProgressBar, 10), null));
        successCountLabel = new Label(shell, SWT.NONE);
        successCountLabel.setLayoutData(LayoutHelper.formData(new FormAttachment(successLabel, -5), new FormAttachment(100), new FormAttachment(tryProgressBar, 10), null));
        successProgressBar = new ProgressBar(shell, SWT.NONE);
        successProgressBar.setLayoutData(LayoutHelper.formData(new FormAttachment(0), new FormAttachment(100), new FormAttachment(successLabel), null));

        startButton = new Button(shell, SWT.NONE);
        startButton.setText(Labels.getLabel("button.start"));
        startButton.addListener(SWT.Selection, new StartButtonListener());

        closeButton = new Button(shell, SWT.NONE);
        closeButton.setText(Labels.getLabel("button.close"));
        closeButton.addListener(SWT.Selection, new Listener()
        {
            public void handleEvent(Event event)
            {
                shell.close();
            }
        });

        positionButtonsInFormLayout(startButton, closeButton, successProgressBar);

        shell.pack();
    }
