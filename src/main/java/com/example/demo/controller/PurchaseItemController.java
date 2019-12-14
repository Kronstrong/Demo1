package com.example.demo.controller;

import com.example.demo.model.PurchaseItem;
import com.example.demo.model.PurchaseItemRepository;
import com.example.demo.model.ShopItem;
import com.example.demo.model.ShopItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class PurchaseItemController {
	private PurchaseItemRepository purchaseItemRepository;
	private ShopItemRepository shopItemRepository;
	
	public PurchaseItemController(PurchaseItemRepository purchaseItemRepository, ShopItemRepository shopItemRepository) {
		this.purchaseItemRepository = purchaseItemRepository;
		this.shopItemRepository = shopItemRepository;
	}
	
	@PostMapping("/purchaseItems")
	@Transactional
	public String createOrUpdate(@RequestBody PurchaseItem item) {
		purchaseItemRepository.save(item);
		return "OK";
	}
	
	@DeleteMapping("/purchaseItems/{id}")
	public String delete(@PathVariable long id) {
		purchaseItemRepository.deleteById(id);
		return "OK";
	}
	
	@GetMapping("/purchaseItems")
	public PurchaseItems getAll() {
		return new PurchaseItems(purchaseItemRepository.findAll());
	}
	
	@GetMapping("/purchaseItems/lastWeek")
	public PurchaseItems getLastWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		return new PurchaseItems(purchaseItemRepository.findByDateAfter(calendar.getTime()));
	}
	
	@GetMapping("/purchaseItems/topBuyer")
	public PurchaseItemRepository.TopBuyerInfo getTopBuyer() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -180);
		return purchaseItemRepository.findTopBuyers(calendar.getTime(), PageRequest.of(0, 1)).get(0);
	}
	
	@GetMapping("/purchaseItems/topProduct")
	public PurchaseItemRepository.TopProductInfo getTopProduct() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		return purchaseItemRepository.findTopProduct(calendar.getTime(), PageRequest.of(0, 1)).get(0);
	}
	
	@GetMapping("/purchaseItems/topProductByAge")
	public PurchaseItemRepository.TopProductInfo getTopProductByAge(@RequestParam int age) {
		return purchaseItemRepository.findTopProductByAge(age, PageRequest.of(0, 1)).get(0);
	}
	
	@PostMapping("/purchaseItems/post")
	@Transactional
	public ResponseEntity<String> postItem(HttpServletRequest request) throws Throwable {
		// Parse XML document to DOM from request input stream
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = parser.parse(request.getInputStream());
		// Validate parsed document
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Source schemaFile = new StreamSource(getClass().getResourceAsStream("/request.xsd"));
		Schema schema = factory.newSchema(schemaFile);
		Validator validator = schema.newValidator();
		try {
			validator.validate(new DOMSource(document));
		} catch (SAXException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		// Extract values from parsed document
		Element root = document.getDocumentElement();
		String firstName = root.getElementsByTagName("first-name").item(0).getTextContent();
		String lastName = root.getElementsByTagName("last-name").item(0).getTextContent();
		int age = Integer.parseInt(root.getElementsByTagName("age").item(0).getTextContent());
		long itemId = Long.parseLong(root.getElementsByTagName("item").item(0).getTextContent());
		int count = Integer.parseInt(root.getElementsByTagName("count").item(0).getTextContent());
		double amount = Double.parseDouble(root.getElementsByTagName("amount").item(0).getTextContent());
		Date date = new SimpleDateFormat("yyyy-MM-dd")
				.parse(root.getElementsByTagName("date").item(0).getTextContent());
		// Find ShopItem by its ID
		Optional<ShopItem> shopItemOptional = shopItemRepository.findById(itemId);
		if (!shopItemOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Shop item not found");
		}
		ShopItem shopItem = shopItemOptional.get();
		// Save new entity to the database
		PurchaseItem purchaseItem = new PurchaseItem();
		purchaseItem.setFirstName(firstName);
		purchaseItem.setLastName(lastName);
		purchaseItem.setAge(age);
		purchaseItem.setShopItem(shopItem);
		purchaseItem.setCount(count);
		purchaseItem.setAmount(amount);
		purchaseItem.setDate(date);
		purchaseItemRepository.save(purchaseItem);
		return ResponseEntity.ok("OK");
	}
	
	public static class PurchaseItems {
		private List<PurchaseItem> data;
		
		public PurchaseItems(List<PurchaseItem> data) {
			this.data = data;
		}
		
		public List<PurchaseItem> getData() {
			return data;
		}
	}
}
