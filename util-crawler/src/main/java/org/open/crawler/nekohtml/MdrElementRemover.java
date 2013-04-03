package org.open.crawler.nekohtml;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.cyberneko.html.filters.ElementRemover;

public class MdrElementRemover extends ElementRemover {
    protected int fNonElementDepth = 0;

    // since Xerces-J 2.2.0

    /** Start prefix mapping. */
    public void startPrefixMapping(String prefix, String uri, Augmentations augs)
        throws XNIException {
        fNonElementDepth++;
    }

    /** End prefix mapping. */
    public void endPrefixMapping(String prefix, Augmentations augs)
        throws XNIException {
        fNonElementDepth--;
    }

    //
    // Protected methods
    //
    
    /** Start element. */
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
        throws XNIException {
        if (fElementDepth <= fRemovalElementDepth && handleOpenTag(element, attributes)) {
            super.startElement(element, attributes, augs);
        }
        fElementDepth++;
    } 

    /** Empty element. */
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
        throws XNIException {
        if (fElementDepth <= fRemovalElementDepth && handleOpenTag(element, attributes)) {
            super.emptyElement(element, attributes, augs);
        }
    } 

    /** Comment. */
    public void comment(XMLString text, Augmentations augs)
        throws XNIException {}

    /** Processing instruction. */
    public void processingInstruction(String target, XMLString data, Augmentations augs)
        throws XNIException {}

    /** Characters. */
    public void characters(XMLString text, Augmentations augs) 
        throws XNIException {
        if (fElementDepth <= fRemovalElementDepth && fNonElementDepth <= 0) {
            if(text.toString().trim().length() > 0) {
               super.characters(text, augs);
           }
        }
    }

    /** Ignorable whitespace. */
    public void ignorableWhitespace(XMLString text, Augmentations augs) 
        throws XNIException {}

    /** Start general entity. */
    public void startGeneralEntity(String name, XMLResourceIdentifier id, String encoding, Augmentations augs)
        throws XNIException {
        fNonElementDepth++;
    } 

    /** Text declaration. */
    public void textDecl(String version, String encoding, Augmentations augs)
        throws XNIException {}

    /** End general entity. */
    public void endGeneralEntity(String name, Augmentations augs)
        throws XNIException {
        fNonElementDepth--;
    } 

    /** Start CDATA section. */
    public void startCDATA(Augmentations augs) throws XNIException {
        fNonElementDepth++;
    } 

    /** End CDATA section. */
    public void endCDATA(Augmentations augs) throws XNIException {
        fNonElementDepth--;
    } 

    /** End element. */
    public void endElement(QName element, Augmentations augs)
        throws XNIException {
        if (fElementDepth <= fRemovalElementDepth && elementAccepted(element.rawname)) {
            super.endElement(element, augs);
        }
        fElementDepth--;
        if (fElementDepth == fRemovalElementDepth) {
            fRemovalElementDepth = Integer.MAX_VALUE;
        }
    } 

    

    /** Returns true if the specified element is accepted. */
    protected boolean elementAccepted(String element) {
        return true;
    } 

    /** Returns true if the specified element should be removed. */
    protected boolean elementRemoved(String element) {
        Object key = element.toLowerCase();
        if (fRemovedElements.get(key) == NULL) {
            return true;
        }
        else 
        {
            return false;
        }
    } 

    /** Handles an open tag. */
    protected boolean handleOpenTag(QName element, XMLAttributes attributes) {
        if (elementRemoved(element.rawname)) {
            fRemovalElementDepth = fElementDepth;
            return false;
        }
        return true;
    }
}
