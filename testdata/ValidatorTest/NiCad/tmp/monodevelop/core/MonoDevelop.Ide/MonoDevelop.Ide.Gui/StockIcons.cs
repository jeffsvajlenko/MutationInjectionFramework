// Stock.cs
//
// Author:
//   Lluis Sanchez Gual <lluis@novell.com>
//
// Copyright (c) 2005 Novell, Inc (http://www.novell.com)
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
//
//


using System;
using MonoDevelop.Core;

namespace MonoDevelop.Ide.Gui
{
public class Stock
{
    public static readonly IconId AddNamespace = "md-add-namespace";
    public static readonly IconId BreakPoint = "md-break-point";
    public static readonly IconId BuildCombine = "md-build-combine";
    public static readonly IconId BuildCurrentSelectedProject = "md-build-current-selected-project";
    public static readonly IconId Class = "md-class";
    public static readonly IconId ClearAllBookmarks = "md-clear-all-bookmarks";
    public static readonly IconId CloseAllDocuments = "md-close-all-documents";
    public static readonly IconId CloseCombine = "md-close-combine-icon";
    public static readonly IconId CloseIcon = Gtk.Stock.Close;
    public static readonly IconId ClosedFolder = "md-closed-folder";
    public static readonly IconId ClosedReferenceFolder = "md-closed-reference-folder";
    public static readonly IconId ClosedResourceFolder = "md-closed-resource-folder";
    public static readonly IconId Solution = "md-solution";
    public static readonly IconId Workspace = "md-workspace";
    public static readonly IconId CopyIcon = Gtk.Stock.Copy;
    public static readonly IconId CutIcon = Gtk.Stock.Cut;
    public static readonly IconId Delegate = "md-delegate";
    public static readonly IconId DeleteIcon = Gtk.Stock.Delete;
    public static readonly IconId Empty = "md-empty";
    public static readonly IconId EmptyFileIcon = "md-empty-file-icon";
    public static readonly IconId Enum = "md-enum";
    public static readonly IconId Error = Gtk.Stock.DialogError;
    public static readonly IconId Event = "md-event";
    public static readonly IconId ExecutionMarker = "md-execution-marker";
    public static readonly IconId Field = "md-field";
    public static readonly IconId FileXmlIcon = "md-file-xml-icon";
    public static readonly IconId FindIcon = Gtk.Stock.Find;
    public static readonly IconId FindInFiles = "md-find-in-files";
    public static readonly IconId FindNextIcon = "md-find-next-icon";
    public static readonly IconId FullScreen = Gtk.Stock.Fullscreen;
    public static readonly IconId GotoNextbookmark = "md-goto-nextbookmark";
    public static readonly IconId GotoPrevbookmark = "md-goto-prevbookmark";
    public static readonly IconId Information = Gtk.Stock.DialogInfo;
    public static readonly IconId Interface = "md-interface";
    public static readonly IconId InternalClass = "md-internal-class";
    public static readonly IconId InternalDelegate = "md-internal-delegate";
    public static readonly IconId InternalEnum = "md-internal-enum";
    public static readonly IconId InternalEvent = "md-internal-event";
    public static readonly IconId InternalField = "md-internal-field";
    public static readonly IconId InternalInterface = "md-internal-interface";
    public static readonly IconId InternalMethod = "md-internal-method";
    public static readonly IconId InternalProperty = "md-internal-property";
    public static readonly IconId InternalStruct = "md-internal-struct";
    public static readonly IconId Literal = "md-literal";
    public static readonly IconId Method = "md-method";
    public static readonly IconId MiscFiles = "md-misc-files";
    public static readonly IconId NameSpace = "md-name-space";
    public static readonly IconId NewDocumentIcon = Gtk.Stock.New;
    public static readonly IconId NewFolderIcon = "md-new-folder-icon";
    public static readonly IconId NewProjectIcon = "md-new-project-icon";
    public static readonly IconId NextWindowIcon = Gtk.Stock.GoForward;
    public static readonly IconId OpenFileIcon = Gtk.Stock.Open;
    public static readonly IconId OpenFolder = "md-open-folder";
    public static readonly IconId OpenProjectIcon = "md-open-project-icon";
    public static readonly IconId OpenReferenceFolder = "md-open-reference-folder";
    public static readonly IconId OpenResourceFolder = "md-open-resource-folder";
    public static readonly IconId Options = Gtk.Stock.Preferences;
    public static readonly IconId OutputIcon = "md-output-icon";
    public static readonly IconId PasteIcon = Gtk.Stock.Paste;
    public static readonly IconId PreView = Gtk.Stock.PrintPreview;
    public static readonly IconId PrevWindowIcon = Gtk.Stock.GoBack;
    public static readonly IconId Print = Gtk.Stock.Print;
    public static readonly IconId PrivateClass = "md-private-class";
    public static readonly IconId PrivateDelegate = "md-private-delegate";
    public static readonly IconId PrivateEnum = "md-private-enum";
    public static readonly IconId PrivateEvent = "md-private-event";
    public static readonly IconId PrivateField = "md-private-field";
    public static readonly IconId PrivateInterface = "md-private-interface";
    public static readonly IconId PrivateMethod = "md-private-method";
    public static readonly IconId PrivateProperty = "md-private-property";
    public static readonly IconId PrivateStruct = "md-private-struct";
    public static readonly IconId PropertiesIcon = "md-properties-icon";
    public static readonly IconId Property = "md-property";
    public static readonly IconId ProtectedClass = "md-protected-class";
    public static readonly IconId ProtectedDelegate = "md-protected-delegate";
    public static readonly IconId ProtectedEnum = "md-protected-enum";
    public static readonly IconId ProtectedEvent = "md-protected-event";
    public static readonly IconId ProtectedField = "md-protected-field";
    public static readonly IconId ProtectedInterface = "md-protected-interface";
    public static readonly IconId ProtectedMethod = "md-protected-method";
    public static readonly IconId ProtectedProperty = "md-protected-property";
    public static readonly IconId ProtectedStruct = "md-protected-struct";
    public static readonly IconId PinDown = "md-pin-down";
    public static readonly IconId PinUp = "md-pin-up";
    public static readonly IconId Question = Gtk.Stock.DialogQuestion;
    public static readonly IconId QuitIcon = Gtk.Stock.Quit;
    public static readonly IconId RedoIcon = Gtk.Stock.Redo;
    public static readonly IconId Reference = "md-reference";
    public static readonly IconId ReplaceIcon = Gtk.Stock.FindAndReplace;
    public static readonly IconId ReplaceInFiles = "md-replace-in-files";
    public static readonly IconId ResourceFileIcon = "md-resource-file-icon";
    public static readonly IconId RunProgramIcon = Gtk.Stock.Execute;
    public static readonly IconId SaveAllIcon = "md-save-all-icon";
    public static readonly IconId SaveAsIcon = Gtk.Stock.SaveAs;
    public static readonly IconId SaveIcon = Gtk.Stock.Save;
    public static readonly IconId MonoDevelop = "md-monodevelop";
    public static readonly IconId Project = "md-project";
    public static readonly IconId SplitWindow = "md-split-window";
    public static readonly IconId Struct = "md-struct";
    public static readonly IconId TaskListIcon = "md-task-list-icon";
    public static readonly IconId TextFileIcon = "md-text-file-icon";
    public static readonly IconId TipOfTheDay = "md-tip-of-the-day";
    public static readonly IconId ToggleBookmark = "md-toggle-bookmark";
    public static readonly IconId UndoIcon = Gtk.Stock.Undo;
    public static readonly IconId Warning = Gtk.Stock.DialogWarning;
    public static readonly IconId WebSearchIcon = "md-web-search-icon";
    public static readonly IconId XmlFileIcon = "md-xml-file-icon";
    public static readonly IconId Addin = "md-addin";
    public static readonly IconId SolutionFolderOpen = "md-solution-folder-open";
    public static readonly IconId SolutionFolderClosed = "md-solution-folder-closed";
    public static readonly IconId Package = "md-package";

}
}
