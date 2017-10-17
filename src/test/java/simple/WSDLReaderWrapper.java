package simple;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.ExtensionDeserializer;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.schema.SchemaReference;
import javax.wsdl.factory.WSDLFactory;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;

import com.ibm.wsdl.Constants;
import com.ibm.wsdl.extensions.schema.SchemaConstants;
import com.ibm.wsdl.util.StringUtils;
import com.ibm.wsdl.util.xml.DOMUtils;
import com.ibm.wsdl.util.xml.QNameUtils;
import com.ibm.wsdl.util.xml.XPathUtils;
import com.ibm.wsdl.xml.WSDLReaderImpl;

public class WSDLReaderWrapper extends WSDLReaderImpl {
	public Map getAllSchemas() {
		return allSchemas;
	}

	File savedDir = new File("G:/workbase/wsdlDownloadLocation");

	public Map<Schema, List<SchemaReference>> getAllSchemaRefMap() {
		Map<Schema, List<SchemaReference>> map = new HashMap<Schema, List<SchemaReference>>();
		ArrayList allSchemaRefs = new ArrayList();
		for (Object object : allSchemas.values()) {
			Schema schema = (Schema) object;
			Collection ic = schema.getImports().values();
			Iterator importsIterator = ic.iterator();
			while (importsIterator.hasNext()) {
				allSchemaRefs.addAll((Collection) importsIterator.next());
			}

			allSchemaRefs.addAll(schema.getIncludes());
			allSchemaRefs.addAll(schema.getRedefines());
			map.put(schema, allSchemaRefs);
		}
		return map;
	}

	private static QName getQualifiedAttributeValue(Element el, String attrName, String elDesc, Definition def,
			List remainingAttrs) throws WSDLException {
		try {
			return DOMUtils.getQualifiedAttributeValue(el, attrName, elDesc, false, def, remainingAttrs);
		} catch (WSDLException e) {
			if (e.getFaultCode().equals(WSDLException.NO_PREFIX_SPECIFIED)) {
				String attrValue = DOMUtils.getAttribute(el, attrName, remainingAttrs);

				return new QName(attrValue);
			} else {
				throw e;
			}
		}
	}

	private static void registerNSDeclarations(NamedNodeMap attrs, Definition def) {
		int size = attrs.getLength();

		for (int i = 0; i < size; i++) {
			Attr attr = (Attr) attrs.item(i);
			String namespaceURI = attr.getNamespaceURI();
			String localPart = attr.getLocalName();
			String value = attr.getValue();

			if (namespaceURI != null && namespaceURI.equals(Constants.NS_URI_XMLNS)) {
				if (localPart != null && !localPart.equals(Constants.ATTR_XMLNS)) {
					DOMUtils.registerUniquePrefix(localPart, value, def);
				} else {
					DOMUtils.registerUniquePrefix(null, value, def);
				}
			}
		}
	}

	public List getAllSchemaRefs() {
		ArrayList allSchemaRefs = new ArrayList();
		for (Object object : allSchemas.values()) {
			Schema schema = (Schema) object;
			Collection ic = schema.getImports().values();
			Iterator importsIterator = ic.iterator();
			while (importsIterator.hasNext()) {
				allSchemaRefs.addAll((Collection) importsIterator.next());
			}

			allSchemaRefs.addAll(schema.getIncludes());
			allSchemaRefs.addAll(schema.getRedefines());
		}
		return allSchemaRefs;
	}

	public InputStream saveInputStreamToFile(InputStream in, String fileName) throws IOException {
		FileOutputStream osStream = new FileOutputStream(new File(savedDir, fileName));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) > -1) {
			baos.write(buffer, 0, len);
			baos.flush();
			osStream.write(buffer, 0, len);
			osStream.flush();
		}

		InputStream copyed = new ByteArrayInputStream(baos.toByteArray());
		osStream.close();
		baos.close();
		return copyed;
	}

	private static Document getDocument(InputSource inputSource, String desc) throws WSDLException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setNamespaceAware(true);
		factory.setValidating(false);

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputSource);

			return doc;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new WSDLException(WSDLException.PARSER_ERROR, "Problem parsing '" + desc + "'.", e);
		}
	}

	/**
	 * Read the WSDL document accessible via the specified URI into a WSDL
	 * definition.
	 *
	 * @param contextURI
	 *            the context in which to resolve the wsdlURI, if the wsdlURI is
	 *            relative. Can be null, in which case it will be ignored.
	 * @param wsdlURI
	 *            a URI (can be a filename or URL) pointing to a WSDL XML
	 *            definition.
	 * @return the definition.
	 */
	public Definition readWSDL(String contextURI, String wsdlURI) throws WSDLException {
		try {
			if (verbose) {
				System.out.println("Retrieving document at '" + wsdlURI + "'"
						+ (contextURI == null ? "." : ", relative to '" + contextURI + "'."));
			}

			URL contextURL = (contextURI != null) ? StringUtils.getURL(null, contextURI) : null;
			URL url = StringUtils.getURL(contextURL, wsdlURI);
			InputStream inputStream = StringUtils.getContentAsInputStream(url);
			/******************************************************/
			String fileName = renameWsdlElement(contextURL, wsdlURI);
			inputStream = saveInputStreamToFile(inputStream, fileName);
			/********************************************************/
			InputSource inputSource = new InputSource(inputStream);
			inputSource.setSystemId(url.toString());
			Document doc = getDocument(inputSource, url.toString());

			inputStream.close();

			Definition def = readWSDL(url.toString(), doc);

			return def;
		} catch (WSDLException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new WSDLException(WSDLException.OTHER_ERROR, "Unable to resolve imported document at '" + wsdlURI
					+ (contextURI == null ? "'." : "', relative to '" + contextURI + "'."), e);
		}
	}

	protected ExtensibilityElement parseSchema(Class parentType, Element el, Definition def, ExtensionRegistry extReg)
			throws WSDLException {
		/*
		 * This method returns ExtensibilityElement rather than Schema because
		 * we do not insist that a suitable XSD schema deserializer is
		 * registered. PopulatedExtensionRegistry registers SchemaDeserializer
		 * by default, but if the user chooses not to register a suitable
		 * deserializer then the UnknownDeserializer will be used, returning an
		 * UnknownExtensibilityElement.
		 */

		Schema schema = null;
		SchemaReference schemaRef = null;
		try {

			QName elementType = QNameUtils.newQName(el);

			ExtensionDeserializer exDS = extReg.queryDeserializer(parentType, elementType);

			// Now unmarshall the DOM element.
			ExtensibilityElement ee = exDS.unmarshall(parentType, elementType, el, def, extReg);

			if (ee instanceof Schema) {
				schema = (Schema) ee;
			} else {
				// Unknown extensibility element, so don't do any more schema
				// parsing on it.
				return ee;
			}

			// Keep track of parsed schemas to avoid duplicating Schema objects
			// through duplicate or circular references (eg: A imports B imports
			// A).
			if (schema.getDocumentBaseURI() != null) {
				this.allSchemas.put(schema.getDocumentBaseURI(), schema);
			}

			// At this point, any SchemaReference objects held by the schema
			// will not
			// yet point to their referenced schemas, so we must now retrieve
			// these
			// schemas and set the schema references.

			// First, combine the schema references for imports, includes and
			// redefines
			// into a single list

			ArrayList allSchemaRefs = new ArrayList();

			Collection ic = schema.getImports().values();
			Iterator importsIterator = ic.iterator();
			while (importsIterator.hasNext()) {
				allSchemaRefs.addAll((Collection) importsIterator.next());
			}

			allSchemaRefs.addAll(schema.getIncludes());
			allSchemaRefs.addAll(schema.getRedefines());

			// Then, retrieve the schema referred to by each schema reference.
			// If the
			// schema has been read in previously, use the existing schema
			// object.
			// Otherwise unmarshall the DOM element into a new schema object.

			ListIterator schemaRefIterator = allSchemaRefs.listIterator();

			while (schemaRefIterator.hasNext()) {
				try {
					schemaRef = (SchemaReference) schemaRefIterator.next();

					if (schemaRef.getSchemaLocationURI() == null) {
						// cannot get the referenced schema, so ignore this
						// schema reference
						continue;
					}

					if (verbose) {
						System.out.println("Retrieving schema at '" + schemaRef.getSchemaLocationURI()
								+ (schema.getDocumentBaseURI() == null ? "'."
										: "', relative to '" + schema.getDocumentBaseURI() + "'."));
					}

					InputStream inputStream = null;
					InputSource inputSource = null;

					// This is the child schema referred to by the
					// schemaReference
					Schema referencedSchema = null;

					// This is the child schema's location obtained from the
					// WSDLLocator or the URL
					String location = null;
					String fileName = null;
					if (loc != null) {
						// Try to get the referenced schema using the wsdl
						// locator
						inputSource = loc.getImportInputSource(schema.getDocumentBaseURI(),
								schemaRef.getSchemaLocationURI());
						fileName = renameWsdlElement(schema.getDocumentBaseURI(), schemaRef.getSchemaLocationURI());
						if (inputSource == null) {
							throw new WSDLException(WSDLException.OTHER_ERROR,
									"Unable to locate with a locator " + "the schema referenced at '"
											+ schemaRef.getSchemaLocationURI() + "' relative to document base '"
											+ schema.getDocumentBaseURI() + "'");
						}
						location = loc.getLatestImportURI();

						// if a schema from this location has been read
						// previously, use it.
						referencedSchema = (Schema) this.allSchemas.get(location);
					} else {
						// We don't have a wsdl locator, so try to retrieve the
						// schema by its URL
						String contextURI = schema.getDocumentBaseURI();
						URL contextURL = (contextURI != null) ? StringUtils.getURL(null, contextURI) : null;
						URL url = StringUtils.getURL(contextURL, schemaRef.getSchemaLocationURI());
						location = url.toExternalForm();
						fileName = renameWsdlElement("", url.toString());
						// if a schema from this location has been retrieved
						// previously, use it.
						referencedSchema = (Schema) this.allSchemas.get(location);

						if (referencedSchema == null) {
							// We haven't read this schema in before so do it
							// now
							inputStream = StringUtils.getContentAsInputStream(url);

							if (inputStream != null) {
								inputSource = new InputSource(inputStream);
							}

							if (inputSource == null) {
								throw new WSDLException(WSDLException.OTHER_ERROR,
										"Unable to locate with a url " + "the document referenced at '"
												+ schemaRef.getSchemaLocationURI() + "'"
												+ (contextURI == null ? "." : ", relative to '" + contextURI + "'."));
							}
						}

					} // end if loc

					// If we have not previously read the schema, get its DOM
					// element now.
					if (referencedSchema == null) {
						inputStream = saveInputStreamToFile(inputStream, fileName);
						inputSource.setSystemId(location);
						inputSource.setByteStream(inputStream);
						Document doc = getDocument(inputSource, location);

						if (inputStream != null) {
							inputStream.close();
						}

						Element documentElement = doc.getDocumentElement();

						// If it's a schema doc process it, otherwise the schema
						// reference remains null

						QName docElementQName = QNameUtils.newQName(documentElement);

						if (SchemaConstants.XSD_QNAME_LIST.contains(docElementQName)) {
							// We now need to call parseSchema recursively to
							// parse the referenced
							// schema. The document base URI of the referenced
							// schema will be set to
							// the document base URI of the current schema plus
							// the schemaLocation in
							// the schemaRef. We cannot explicitly pass in a new
							// document base URI
							// to the schema deserializer, so instead we will
							// create a dummy
							// Definition and set its documentBaseURI to the new
							// document base URI.
							// We can leave the other definition fields empty
							// because we know
							// that the SchemaDeserializer.unmarshall method
							// uses the definition
							// parameter only to get its documentBaseURI. If the
							// unmarshall method
							// implementation changes (ie: its use of definition
							// changes) we may need
							// to rethink this approach.

							WSDLFactory factory = getWSDLFactory();
							Definition dummyDef = factory.newDefinition();

							dummyDef.setDocumentBaseURI(location);

							// By this point, we know we have a
							// SchemaDeserializer registered
							// so we can safely cast the ExtensibilityElement to
							// a Schema.
							referencedSchema = (Schema) parseSchema(parentType, documentElement, dummyDef, extReg);
						}

					} // end if referencedSchema

					schemaRef.setReferencedSchema(referencedSchema);
				} catch (WSDLException e) {
					throw e;
				} catch (RuntimeException e) {
					throw e;
				} catch (Exception e) {
					throw new WSDLException(WSDLException.OTHER_ERROR,
							"An error occurred trying to resolve schema referenced at '"
									+ schemaRef.getSchemaLocationURI() + "'" + (schema.getDocumentBaseURI() == null
											? "." : ", relative to '" + schema.getDocumentBaseURI() + "'."),
							e);
				}

			} // end while loop

			return schema;

		} catch (WSDLException e) {
			if (e.getLocation() == null) {
				e.setLocation(XPathUtils.getXPathExprFromNode(el));
			} else {
				// If this method has been called recursively for nested schemas
				// the exception location must be built up recursively too so
				// prepend this element's xpath to exception location.
				String loc = XPathUtils.getXPathExprFromNode(el) + e.getLocation();
				e.setLocation(loc);
			}

			throw e;
		}

	}

	protected Import parseImport(Element importEl, Definition def, Map importedDefs) throws WSDLException {
		Import importDef = def.createImport();
		String fileName = null;
		try {
			String namespaceURI = DOMUtils.getAttribute(importEl, Constants.ATTR_NAMESPACE);
			String locationURI = DOMUtils.getAttribute(importEl, Constants.ATTR_LOCATION);
			String contextURI = null;

			if (namespaceURI != null) {
				importDef.setNamespaceURI(namespaceURI);
			}

			if (locationURI != null) {
				importDef.setLocationURI(locationURI);

				if (importDocuments) {
					try {
						contextURI = def.getDocumentBaseURI();
						Definition importedDef = null;
						InputStream inputStream = null;
						InputSource inputSource = null;
						URL url = null;
						
						if (loc != null) {
							inputSource = loc.getImportInputSource(contextURI, locationURI);
							fileName = renameWsdlElement(contextURI, locationURI);
							/*
							 * We now have available the latest import URI. This
							 * might differ from the locationURI so check the
							 * importedDefs for it since it is this that we pass
							 * as the documentBaseURI later.
							 */
							String liu = loc.getLatestImportURI();

							importedDef = (Definition) importedDefs.get(liu);

							inputSource.setSystemId(liu);
						} else {
							URL contextURL = (contextURI != null) ? StringUtils.getURL(null, contextURI) : null;
							
							url = StringUtils.getURL(contextURL, locationURI);
							fileName = renameWsdlElement(contextURI, locationURI);
							importedDef = (Definition) importedDefs.get(url.toString());

							if (importedDef == null) {
								inputStream = StringUtils.getContentAsInputStream(url);
								fileName = renameWsdlElement(contextURI, locationURI);
								if (inputStream != null) {
									inputSource = new InputSource(inputStream);
									inputSource.setSystemId(url.toString());
								}
							}
						}

						if (importedDef == null) {
							if (inputSource == null) {
								throw new WSDLException(WSDLException.OTHER_ERROR,
										"Unable to locate imported document " + "at '" + locationURI + "'"
												+ (contextURI == null ? "." : ", relative to '" + contextURI + "'."));
							}
							
							inputStream = saveInputStreamToFile(inputStream, fileName);
							inputSource.setByteStream(inputStream);
							Document doc = getDocument(inputSource, inputSource.getSystemId());

							if (inputStream != null) {
								inputStream.close();
							}

							Element documentElement = doc.getDocumentElement();

							/*
							 * Check if it's a wsdl document. If it's not, don't
							 * retrieve and process it. This should later be
							 * extended to allow other types of documents to be
							 * retrieved and processed, such as schema documents
							 * (".xsd"), etc...
							 */
							if (QNameUtils.matches(Constants.Q_ELEM_DEFINITIONS, documentElement)) {
								if (verbose) {
									System.out.println("Retrieving document at '" + locationURI + "'"
											+ (contextURI == null ? "." : ", relative to '" + contextURI + "'."));
								}

								String urlString = (loc != null) ? loc.getLatestImportURI()
										: (url != null) ? url.toString() : locationURI;

								importedDef = readWSDL(urlString, documentElement, importedDefs);
							} else {
								QName docElementQName = QNameUtils.newQName(documentElement);

								if (SchemaConstants.XSD_QNAME_LIST.contains(docElementQName)) {
									if (verbose) {
										System.out.println("Retrieving schema wsdl:imported from '" + locationURI + "'"
												+ (contextURI == null ? "." : ", relative to '" + contextURI + "'."));
									}

									WSDLFactory factory = getWSDLFactory();

									importedDef = factory.newDefinition();

									if (extReg != null) {
										importedDef.setExtensionRegistry(extReg);
									}

									String urlString = (loc != null) ? loc.getLatestImportURI()
											: (url != null) ? url.toString() : locationURI;

									importedDef.setDocumentBaseURI(urlString);

									Types types = importedDef.createTypes();
									types.addExtensibilityElement(
											parseSchema(Types.class, documentElement, importedDef));
									importedDef.setTypes(types);
								}
							}
						}

						if (importedDef != null) {
							importDef.setDefinition(importedDef);
						}
					} catch (WSDLException e) {
						throw e;
					} catch (RuntimeException e) {
						throw e;
					} catch (Exception e) {
						throw new WSDLException(WSDLException.OTHER_ERROR, "Unable to resolve imported document at '"
								+ locationURI + (contextURI == null ? "'." : "', relative to '" + contextURI + "'"), e);
					}
				} // end importDocs
			} // end locationURI

		} catch (WSDLException e) {
			if (e.getLocation() == null) {
				e.setLocation(XPathUtils.getXPathExprFromNode(importEl));
			} else {
				// If definitions are being parsed recursively for nested
				// imports
				// the exception location must be built up recursively too so
				// prepend this element's xpath to exception location.
				String loc = XPathUtils.getXPathExprFromNode(importEl) + e.getLocation();
				e.setLocation(loc);
			}

			throw e;
		}

		// register any NS decls with the Definition
		NamedNodeMap attrs = importEl.getAttributes();
		registerNSDeclarations(attrs, def);

		Element tempEl = DOMUtils.getFirstChildElement(importEl);

		while (tempEl != null) {
			if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl)) {
				importDef.setDocumentationElement(tempEl);
			} else {
				importDef.addExtensibilityElement(parseExtensibilityElement(Import.class, tempEl, def));
			}

			tempEl = DOMUtils.getNextSiblingElement(tempEl);
		}

		parseExtensibilityAttributes(importEl, Import.class, importDef, def);

		return importDef;

	}
	public String renameWsdlElement(Object contextURL,String spec) throws MalformedURLException{
		URL url = null;
		if(contextURL == null || "".equals(contextURL)){
			 url = StringUtils.getURL(null, spec);
		}else if(contextURL instanceof URL){
			url = StringUtils.getURL((URL) contextURL, spec);
		}else{
			URL u = new URL((String) contextURL);
			url = StringUtils.getURL(u, spec);
		}
		String path = url.getPath();
		if(path.startsWith("/")){
			path = path.substring(1);
		}
		String name = path+"_"+url.getQuery();
		name= name.replaceAll("\\W", "_");
		return name;
	}
	public static void main(String[] args) throws MalformedURLException{
		WSDLReaderWrapper reader = new WSDLReaderWrapper();
		System.out.println(reader.renameWsdlElement("", "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl"));;
	}
}
