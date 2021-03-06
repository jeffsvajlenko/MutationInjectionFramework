// Permission is hereby , free of , to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without , including
// without limitation the rights to , , , , ,
// , , and/or sell copies of the , and to
// permit persons to whom the Software is furnished to do , subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY ,
// EXPRESS OR , INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// , FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY , DAMAGES OR OTHER , WHETHER IN AN ACTION
// OF , TORT OR , ARISING , OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// Copyright (c) 2005 , Inc. (http://www.novell.com)
//
// Authors:
//	Peter Bartok	(pbartok@novell.com)
//
//

// COMPLETE

namespace System.Windows.Forms.RTF
{

internal enum Minor
{
    Undefined,

    Skip,

    // Major.CharSet
    AnsiCharSet,
    MacCharSet,
    PcCharSet,
    PcaCharSet,

    // Major.Destinan
    FontTbl,
    FontAltName,
    EmbeddedFont,
    FontFile,
    FileTbl,
    FileInfo,
    ColorTbl,
    StyleSheet,
    KeyCode,
    RevisionTbl,
    Info,
    ITitle,
    ISubject,
    IAuthor,
    IOperator,
    IKeywords,
    IComment,
    IVersion,
    IDoccomm,
    IVerscomm,
    NextFile,
    Template,
    FNSep,
    FNContSep,
    FNContNotice,
    ENSep,
    ENContSep,
    ENContNotice,
    PageNumLevel,
    ParNumLevelStyle,
    Header,
    Footer,
    HeaderLeft,
    HeaderRight,
    HeaderFirst,
    FooterLeft,
    FooterRight,
    FooterFirst,
    ParNumText,
    ParNumbering,
    ParNumTextAfter,
    ParNumTextBefore,
    BookmarkStart,
    BookmarkEnd,
    Pict,
    Object,
    ObjClass,
    ObjName,
    ObjTime,
    ObjData,
    ObjAlias,
    ObjSection,
    ObjResult,
    ObjItem,
    ObjTopic,
    DrawObject,
    Footnote,
    AnnotRefStart,
    AnnotRefEnd,
    AnnotID,
    AnnotAuthor,
    Annotation,
    AnnotRef,
    AnnotTime,
    AnnotIcon,
    Field,
    FieldInst,
    FieldResult,
    DataField,
    Index,
    IndexText,
    IndexRange,
    TOC,
    NeXTGraphic,
    MaxDestination,

    // Major.FontFamily
    FFNil,
    FFRoman,
    FFSwiss,
    FFModern,
    FFScript,
    FFDecor,
    FFTech,
    FFBidirectional,

    // Major.ColorName
    Red,
    Green,
    Blue,

    // Major.SpecialChar
    IIntVersion,
    ICreateTime,
    IRevisionTime,
    IPrintTime,
    IBackupTime,
    IEditTime,
    IYear,
    IMonth,
    IDay,
    IHour,
    IMinute,
    ISecond,
    INPages,
    INWords,
    INChars,
    IIntID,
    CurHeadDate,
    CurHeadDateLong,
    CurHeadDateAbbrev,
    CurHeadTime,
    CurHeadPage,
    SectNum,
    CurFNote,
    CurAnnotRef,
    FNoteSep,
    FNoteCont,
    Cell,
    Row,
    Par,
    Sect,
    Page,
    Column,
    Line,
    SoftPage,
    SoftColumn,
    SoftLine,
    SoftLineHt,
    Tab,
    EmDash,
    EnDash,
    EmSpace,
    EnSpace,
    Bullet,
    LQuote,
    RQuote,
    LDblQuote,
    RDblQuote,
    Formula,
    NoBrkSpace,
    NoReqHyphen,
    NoBrkHyphen,
    OptDest,
    LTRMark,
    RTLMark,
    NoWidthJoiner,
    NoWidthNonJoiner,
    CurHeadPict,

    // Major.StyleAttr
    Additive,
    BasedOn,
    Next,

    // Major.DocAttr
    DefTab,
    HyphHotZone,
    HyphConsecLines,
    HyphCaps,
    HyphAuto,
    LineStart,
    FracWidth,
    MakeBackup,
    RTFDefault,
    PSOverlay,
    DocTemplate,
    DefLanguage,
    FENoteType,
    FNoteEndSect,
    FNoteEndDoc,
    FNoteText,
    FNoteBottom,
    ENoteEndSect,
    ENoteEndDoc,
    ENoteText,
    ENoteBottom,
    FNoteStart,
    ENoteStart,
    FNoteRestartPage,
    FNoteRestart,
    FNoteRestartCont,
    ENoteRestart,
    ENoteRestartCont,
    FNoteNumArabic,
    FNoteNumLLetter,
    FNoteNumULetter,
    FNoteNumLRoman,
    FNoteNumURoman,
    FNoteNumChicago,
    ENoteNumArabic,
    ENoteNumLLetter,
    ENoteNumULetter,
    ENoteNumLRoman,
    ENoteNumURoman,
    ENoteNumChicago,
    PaperWidth,
    PaperHeight,
    PaperSize,
    LeftMargin,
    RightMargin,
    TopMargin,
    BottomMargin,
    FacingPage,
    GutterWid,
    MirrorMargin,
    Landscape,
    PageStart,
    WidowCtrl,
    LinkStyles,
    NoAutoTabIndent,
    WrapSpaces,
    PrintColorsBlack,
    NoExtraSpaceRL,
    NoColumnBalance,
    CvtMailMergeQuote,
    SuppressTopSpace,
    SuppressPreParSpace,
    CombineTblBorders,
    TranspMetafiles,
    SwapBorders,
    ShowHardBreaks,
    FormProtected,
    AllProtected,
    FormShading,
    FormDisplay,
    PrintData,
    RevProtected,
    Revisions,
    RevDisplay,
    RevBar,
    AnnotProtected,
    RTLDoc,
    LTRDoc,

    // Major.SectAttr

    SectDef,
    ENoteHere,
    PrtBinFirst,
    PrtBin,
    SectStyleNum,
    NoBreak,
    ColBreak,
    PageBreak,
    EvenBreak,
    OddBreak,
    Columns,
    ColumnSpace,
    ColumnNumber,
    ColumnSpRight,
    ColumnWidth,
    ColumnLine,
    LineModulus,
    LineDist,
    LineStarts,
    LineRestart,
    LineRestartPg,
    LineCont,
    SectPageWid,
    SectPageHt,
    SectMarginLeft,
    SectMarginRight,
    SectMarginTop,
    SectMarginBottom,
    SectMarginGutter,
    SectLandscape,
    TitleSpecial,
    HeaderY,
    FooterY,
    PageStarts,
    PageCont,
    PageRestart,
    PageNumRight,
    PageNumTop,
    PageDecimal,
    PageURoman,
    PageLRoman,
    PageULetter,
    PageLLetter,
    PageNumHyphSep,
    PageNumSpaceSep,
    PageNumColonSep,
    PageNumEmdashSep,
    PageNumEndashSep,
    TopVAlign,
    BottomVAlign,
    CenterVAlign,
    JustVAlign,
    RTLSect,
    LTRSect,

    // Major.TblAttr
    RowDef,
    RowGapH,
    CellPos,
    MergeRngFirst,
    MergePrevious,
    RowLeft,
    RowRight,
    RowCenter,
    RowLeftEdge,
    RowHt,
    RowHeader,
    RowKeep,
    RTLRow,
    LTRRow,
    RowBordTop,
    RowBordLeft,
    RowBordBottom,
    RowBordRight,
    RowBordHoriz,
    RowBordVert,
    CellBordBottom,
    CellBordTop,
    CellBordLeft,
    CellBordRight,
    CellShading,
    CellBgPatH,
    CellBgPatV,
    CellFwdDiagBgPat,
    CellBwdDiagBgPat,
    CellHatchBgPat,
    CellDiagHatchBgPat,
    CellDarkBgPatH,
    CellDarkBgPatV,
    CellFwdDarkBgPat,
    CellBwdDarkBgPat,
    CellDarkHatchBgPat,
    CellDarkDiagHatchBgPat,
    CellBgPatLineColor,
    CellBgPatColor,

    // Major.ParAttr
    ParDef,
    StyleNum,
    Hyphenate,
    InTable,
    Keep,
    NoWidowControl,
    KeepNext,
    OutlineLevel,
    NoLineNum,
    PBBefore,
    SideBySide,
    QuadLeft,
    QuadRight,
    QuadJust,
    QuadCenter,
    FirstIndent,
    LeftIndent,
    RightIndent,
    SpaceBefore,
    SpaceAfter,
    SpaceBetween,
    SpaceMultiply,
    SubDocument,
    RTLPar,
    LTRPar,
    TabPos,
    TabLeft,
    TabRight,
    TabCenter,
    TabDecimal,
    TabBar,
    LeaderDot,
    LeaderHyphen,
    LeaderUnder,
    LeaderThick,
    LeaderEqual,
    ParLevel,
    ParBullet,
    ParSimple,
    ParNumCont,
    ParNumOnce,
    ParNumAcross,
    ParHangIndent,
    ParNumRestart,
    ParNumCardinal,
    ParNumDecimal,
    ParNumULetter,
    ParNumURoman,
    ParNumLLetter,
    ParNumLRoman,
    ParNumOrdinal,
    ParNumOrdinalText,
    ParNumBold,
    ParNumItalic,
    ParNumAllCaps,
    ParNumSmallCaps,
    ParNumUnder,
    ParNumDotUnder,
    ParNumDbUnder,
    ParNumNoUnder,
    ParNumWordUnder,
    ParNumStrikethru,
    ParNumForeColor,
    ParNumFont,
    ParNumFontSize,
    ParNumIndent,
    ParNumSpacing,
    ParNumInclPrev,
    ParNumCenter,
    ParNumLeft,
    ParNumRight,
    ParNumStartAt,
    BorderTop,
    BorderBottom,
    BorderLeft,
    BorderRight,
    BorderBetween,
    BorderBar,
    BorderBox,
    BorderSingle,
    BorderThick,
    BorderShadow,
    BorderDouble,
    BorderDot,
    BorderDash,
    BorderHair,
    BorderWidth,
    BorderColor,
    BorderSpace,
    Shading,
    BgPatH,
    BgPatV,
    FwdDiagBgPat,
    BwdDiagBgPat,
    HatchBgPat,
    DiagHatchBgPat,
    DarkBgPatH,
    DarkBgPatV,
    FwdDarkBgPat,
    BwdDarkBgPat,
    DarkHatchBgPat,
    DarkDiagHatchBgPat,
    BgPatLineColor,
    BgPatColor,

    // Major.CharAttr
    Plain,
    Bold,
    AllCaps,
    Deleted,
    SubScript,
    SubScrShrink,
    NoSuperSub,
    Expand,
    ExpandTwips,
    Kerning,
    FontNum,
    FontSize,
    Italic,
    Outline,
    Revised,
    RevAuthor,
    RevDTTM,
    SmallCaps,
    Shadow,
    StrikeThru,
    Underline,
    DotUnderline,
    DbUnderline,
    NoUnderline,
    WordUnderline,
    SuperScript,
    SuperScrShrink,
    Invisible,
    ForeColor,
    BackColor,
    RTLChar,
    LTRChar,
    CharStyleNum,
    CharCharSet,
    Language,
    Gray,

    // Major.PictAttr
    MacQD,
    PMMetafile,
    WinMetafile,
    DevIndBitmap,
    WinBitmap,
    PngBlip,
    PixelBits,
    BitmapPlanes,
    BitmapWid,
    PicWid,
    PicHt,
    PicGoalWid,
    PicGoalHt,
    PicScaleX,
    PicScaleY,
    PicScaled,
    PicCropTop,
    PicCropBottom,
    PicCropLeft,
    PicCropRight,
    PicMFHasBitmap,
    PicMFBitsPerPixel,
    PicBinary,

    // Major.BookmarkAttr
    BookmarkFirstCol,
    BookmarkLastCol ,

    // Major.NeXTGrAttr
    NeXTGWidth,
    NeXTGHeight,

    // Major.FieldAttr
    FieldDirty,
    FieldEdited,
    FieldLocked,
    FieldPrivate,
    FieldAlt,

    // Major.TOCAttr
    TOCType,
    TOCLevel,

    // Major.PosAttr
    AbsWid,
    AbsHt,
    RPosMargH,
    RPosPageH,
    RPosColH,
    PosX,
    PosNegX,
    PosXCenter,
    PosXInside,
    PosXOutSide,
    PosXRight,
    PosXLeft,
    RPosMargV,
    RPosPageV,
    RPosParaV,
    PosY,
    PosNegY,
    PosYInline,
    PosYTop,
    PosYCenter,
    PosYBottom,
    NoWrap,
    DistFromTextAll,
    DistFromTextX,
    DistFromTextY,
    TextDistY,
    DropCapLines,
    DropCapType,

    // Major.ObjAttr
    ObjEmb,
    ObjLink,
    ObjAutoLink,
    ObjSubscriber,
    ObjPublisher,
    ObjICEmb,
    ObjLinkSelf,
    ObjLock,
    ObjUpdate,
    ObjHt,
    ObjWid,
    ObjSetSize,
    ObjAlign,
    ObjTransposeY,
    ObjCropTop,
    ObjCropBottom,
    ObjCropLeft,
    ObjCropRight,
    ObjScaleX,
    ObjScaleY,
    ObjResRTF,
    ObjResPict,
    ObjResBitmap,
    ObjResText,
    ObjResMerge,
    ObjBookmarkPubObj,
    ObjPubAutoUpdate,

    // Major.FNoteAttr
    FNAlt,

    // Major.KeyCodeAttr
    AltKey,
    ShiftKey,
    ControlKey,
    FunctionKey,

    // Major.ACharAttr
    ACBold,
    ACAllCaps,
    ACForeColor,
    ACSubScript,
    ACExpand,
    ACFontNum,
    ACFontSize,
    ACItalic,
    ACLanguage,
    ACOutline,
    ACSmallCaps,
    ACShadow,
    ACStrikeThru,
    ACUnderline,
    ACDotUnderline,
    ACDbUnderline,
    ACNoUnderline,
    ACWordUnderline,
    ACSuperScript,

    // Major.FontAttr
    FontCharSet,
    FontPitch,
    FontCodePage,
    FTypeNil,
    FTypeTrueType,

    // Major.FileAttr
    FileNum,
    FileRelPath,
    FileOSNum,

    // Major.FileSource
    SrcMacintosh,
    SrcDOS,
    SrcNTFS,
    SrcHPFS,
    SrcNetwork,

    // Major.DrawAttr
    DrawLock,
    DrawPageRelX,
    DrawColumnRelX,
    DrawMarginRelX,
    DrawPageRelY,
    DrawColumnRelY,
    DrawMarginRelY,
    DrawHeight,
    DrawBeginGroup,
    DrawGroupCount,
    DrawEndGroup,
    DrawArc,
    DrawCallout,
    DrawEllipse,
    DrawLine,
    DrawPolygon,
    DrawPolyLine,
    DrawRect,
    DrawTextBox,
    DrawOffsetX,
    DrawSizeX,
    DrawOffsetY,
    DrawSizeY,
    COAngle,
    COAccentBar,
    COBestFit,
    COBorder,
    COAttachAbsDist,
    COAttachBottom,
    COAttachCenter,
    COAttachTop,
    COLength,
    CONegXQuadrant,
    CONegYQuadrant,
    COOffset,
    COAttachSmart,
    CODoubleLine,
    CORightAngle,
    COSingleLine,
    COTripleLine,
    DrawTextBoxMargin,
    DrawTextBoxText,
    DrawRoundRect,
    DrawPointX,
    DrawPointY,
    DrawPolyCount,
    DrawArcFlipX,
    DrawArcFlipY,
    DrawLineBlue,
    DrawLineGreen,
    DrawLineRed,
    DrawLinePalette,
    DrawLineDashDot,
    DrawLineDashDotDot,
    DrawLineDash,
    DrawLineDot,
    DrawLineGray,
    DrawLineHollow,
    DrawLineSolid,
    DrawLineWidth,
    DrawHollowEndArrow,
    DrawEndArrowLength,
    DrawSolidEndArrow,
    DrawEndArrowWidth,
    DrawHollowStartArrow,
    DrawStartArrowLength,
    DrawSolidStartArrow,
    DrawStartArrowWidth,
    DrawBgFillBlue,
    DrawBgFillGreen,
    DrawBgFillRed,
    DrawBgFillPalette,
    DrawBgFillGray,
    DrawFgFillBlue,
    DrawFgFillGreen,
    DrawFgFillRed,
    DrawFgFillPalette,
    DrawFgFillGray,
    DrawFillPatIndex,
    DrawShadow,
    DrawShadowXOffset,
    DrawShadowYOffset,

    // Major.IndexAttr
    IndexNumber,
    IndexBold,
    IndexItalic,

    // Major.Unicode
    UnicodeCharBytes,
    UnicodeChar,
    UnicodeDestination,
    UnicodeDualDestination,
    UnicodeAnsiCodepage

}
}
