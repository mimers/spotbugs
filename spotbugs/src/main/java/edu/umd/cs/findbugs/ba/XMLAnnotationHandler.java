package edu.umd.cs.findbugs.ba;

import java.util.Objects;
import edu.umd.cs.findbugs.ba.npe.TypeQualifierNullnessAnnotationDatabase;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class XMLAnnotationHandler implements ContentHandler {
    private final TypeQualifierNullnessAnnotationDatabase nullnessAnnotationDatabase;
    private final CheckReturnAnnotationDatabase checkReturnAnnotationDatabase;

    private String currentAnnotationType;
    private String currentClassName;
    private String currentMethodName;
    private String currentMethodSignature;
    private String currentFieldName;
    private boolean isCurrentMethodStatic;

    public XMLAnnotationHandler(TypeQualifierNullnessAnnotationDatabase nullnessAnnotationDatabase,
                                CheckReturnAnnotationDatabase checkReturnAnnotationDatabase) {
        this.nullnessAnnotationDatabase = nullnessAnnotationDatabase;
        this.checkReturnAnnotationDatabase = checkReturnAnnotationDatabase;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        switch (qName) {
            case "Class":
                handleClassElement(attrs);
                break;
            case "Method":
                handleMethodElement(attrs);
                break;
            case "Field":
                handleFieldElement(attrs);
                break;
            case "Parameter":
                handleParameterElement(attrs);
                break;
            case "Nullness":
            case "CheckReturn":
                currentAnnotationType = qName;
                break;
        }
    }

    private void handleParameterElement(Attributes attrs) {
        int currentParameterIndex = Integer.parseInt(attrs.getValue("index"));
        String annotation = attrs.getValue("annotation");

        // 处理Parameter标签，使用addMethodParameterAnnotation
        if (isInNullness() && annotation != null) {
            nullnessAnnotationDatabase.addMethodParameterAnnotation(currentClassName, currentMethodName,
                    currentMethodSignature, false, currentParameterIndex, Objects.requireNonNull(NullnessAnnotation.Parser.parse(annotation)));
        }
    }

    private void handleFieldElement(Attributes attrs) {
        currentFieldName = attrs.getValue("name");
        boolean isStatic = "true".equals(attrs.getValue("static"));
        String annotation = attrs.getValue("annotation");
        if (isInNullness() && annotation != null) {
            nullnessAnnotationDatabase.addFieldAnnotation(currentClassName, currentFieldName,
                    attrs.getValue("signature"), isStatic, Objects.requireNonNull(NullnessAnnotation.Parser.parse(annotation)));
        }
    }

    private void handleMethodElement(Attributes attrs) {
        currentMethodName = attrs.getValue("name");
        currentMethodSignature = attrs.getValue("signature");
        isCurrentMethodStatic = "true".equals(attrs.getValue("static"));

        if (isInCheckReturn()) {
            String priority = attrs.getValue("priority");
            checkReturnAnnotationDatabase.addMethodAnnotation(currentClassName, currentMethodName,
                    currentMethodSignature, isCurrentMethodStatic, Objects.requireNonNull(CheckReturnValueAnnotation.parse(priority)));
        } else if (isInNullness()) {
            String annotation = attrs.getValue("annotation");
            if (annotation != null) {
                nullnessAnnotationDatabase.addMethodAnnotation(currentClassName, currentMethodName,
                        currentMethodSignature, isCurrentMethodStatic, Objects.requireNonNull(NullnessAnnotation.Parser.parse(annotation)));
            }
        }
    }

    private void handleClassElement(Attributes attrs) {
        currentClassName = Objects.requireNonNull(attrs.getValue("name"));
        String currentTarget = attrs.getValue("target");
        String annotation = attrs.getValue("annotation");
        if (isInNullness() && currentTarget != null && annotation != null) {
            nullnessAnnotationDatabase.addDefaultAnnotation(
                    AnnotationDatabase.Target.valueOf(currentTarget),
                    currentClassName,
                    Objects.requireNonNull(NullnessAnnotation.Parser.parse(annotation))
            );
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "Class":
                currentClassName = null;
                break;
            case "Method":
                currentMethodName = null;
                currentMethodSignature = null;
                isCurrentMethodStatic = false;
                break;
            case "Field":
                currentFieldName = null;
                break;
            case "Nullness":
            case "CheckReturn":
                currentAnnotationType = null;
                break;
        }
    }

    private boolean isInNullness() {
        return currentAnnotationType.contains("Nullness");
    }

    private boolean isInCheckReturn() {
        return currentAnnotationType.contains("CheckReturn");
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        // 不需要实现
    }

    @Override
    public void startDocument() {
        // 初始化处理
    }

    @Override
    public void endDocument() {
        // 文档处理结束
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) {
        // 不需要特殊处理
    }

    @Override
    public void endPrefixMapping(String prefix) {
        // 不需要特殊处理
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // XML中没有需要处理的文本内容
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) {
        // 忽略空白
    }

    @Override
    public void processingInstruction(String target, String data) {
        // 不需要处理
    }

    @Override
    public void skippedEntity(String name) {
        // 不需要处理
    }
}
