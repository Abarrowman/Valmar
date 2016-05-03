package game;

import game.aml.*;
import game.rpg.*;

import java.applet.Applet;
//import java.awt.Color;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class ValmarApplet extends Applet implements Runnable, RenderableParent, ValmarRender {
	private static final long serialVersionUID = 1L;
	private Dimension dim;
	private Image offScreen;
	private Graphics bufferGraphics;
	private boolean running;
	private Thread thread;
	private Vector<Renderable> children;

	// important stuff
	public ImageCache cache;
	public ValmarListener listener;
	public World world;
	public ValmarUI ui;
	public Inventory inv;
	public Catalog catalog;

	public ValmarApplet() {
	}

	/**
	 * loadAssets load all the assets needed for Valmar.
	 */
	private void loadAssets() {
		// create image cache
		cache = new ImageCache(this);
		// load catalog
		loadCatalog();
		// load the map
		String zone;
		try {
			zone = TextLoader.loadText(new URL(getCodeBase(), "scripts/map.txt"));
			loadMap(zone);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// load the images
		loadImages();
	}

	/**
	 * init initialises Valmar.
	 */
	public void init() {
		// setup screen
		dim = new Dimension(500, 450);
		resize(dim);

		// setup render
		offScreen = createImage(dim.width, dim.height);
		bufferGraphics = offScreen.getGraphics();
		children = new Vector<Renderable>();

		// add key listener
		listener = new ValmarListener();
		addKeyListener(listener);
		addMouseListener(listener);
		addMouseMotionListener(listener);

		// load all of the assets and unpack them
		loadAssets();
		
		// create the ui
		ui = new ValmarUI(this);
		addChild(ui);

		// create the inventory
		inv = new Inventory(this);
		inv.visible = false;
		inv.x = 115;
		inv.y = 25;
		addChild(inv);

		// setup cursor
		Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(cursor);

		// start thread
		running = true;
		thread = new Thread(this);
		thread.start();

	}

	public void paint(Graphics g) {
		// clear the buffer

		// draw things onto the buffer
		if (cache.isDone()) {
			// draws the renderables
			for (int n = 0; n < children.size(); n++) {
				children.get(n).render(bufferGraphics);
			}

			// draws the message
			String message = "";
			message += world.messageString;
			bufferGraphics.setColor(Color.BLACK);
			drawMultilineText(bufferGraphics, message, 0, 0);
		} else {
			// clears the buffer
			bufferGraphics.clearRect(0, 0, 150, 30);

			// draws loading progress
			String message = "Loading ";
			int count = 0;
			for (int n = 0; n < cache.getNumberOfImage(); n++) {
				if (cache.isDone(n)) {
					count++;
				}
			}
			message += count + "/" + (cache.getNumberOfImage() - 1);
			drawMultilineText(bufferGraphics, message, 0, 0);
		}

		// draw the buffer to screen
		g.drawImage(offScreen, 0, 0, this);
	}

	public void tick() {
		//resize
		if(getSize().width!=dim.width){
			resize(dim);
		}
		// world does its stuff
		world.tick();
		inv.tick();
		ui.tick();
		listener.tick();
		// re render
		repaint();
	}

	private final long SLEEP_PRECISION = TimeUnit.MILLISECONDS.toNanos(2);

	public void run() {
		while (running) {
			tick();
			// less processor intensive than Thread.sleep(50);
			try {
				long end = System.nanoTime() + 50000000;
				long timeLeft = 50000000;
				while (timeLeft > 0) {
					if (timeLeft > SLEEP_PRECISION) {
						Thread.sleep(1);
					} else {
						Thread.sleep(0);
					}					timeLeft = end - System.nanoTime();
					if (Thread.interrupted()) {
						throw new InterruptedException();
					}
				}
				// Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Renderable removeChild(Renderable child) {
		int index = children.indexOf(child);
		if (index != -1) {
			child.parent = null;
			children.remove(child);
		}
		return child;
	}

	public Renderable addChild(Renderable child) {
		int index = children.indexOf(child);
		if (index == -1) {
			child.parent = this;
			children.add(child);
		}
		return child;
	}

	// Applet methods
	public void destroy() {
		// garbage collection
		running = false;
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void drawMultilineText(Graphics g, String output, int x, int y) {
		drawMultilineText(g, output, x, y, false,false);
	}
	public void drawMultilineText(Graphics g, String output, int x, int y, boolean center) {
		drawMultilineText(g, output, x, y, center,false);
	}
	public void drawMultilineText(Graphics g, String output, int x, int y, boolean center, boolean border) {
		String[] lines = output.split("\n");
		int bottom = 0;
		FontMetrics fontMets = g.getFontMetrics();
		for (int n = 0; n < lines.length; n++) {
			String line = lines[n];

			// get bounds
			Rectangle bounds = fontMets.getStringBounds(line, g).getBounds();

			// add to height
			bottom += bounds.height;

			// positioning
			int lineY = y + bottom;
			int lineX = x;
			if (center) {
				lineX -= bounds.width / 2;
			}

			// draw string
			g.drawString(line, lineX, lineY);

			// draw the border
			if(border){
				g.drawRect(lineX, lineY-bounds.height, bounds.width,
				bounds.height);
			}
		}

	}

	public void trace(String output) {
		ui.output.addLine(output);
	}

	public void backToGame() {
		world.active = true;
		world.visible = true;
		inv.visible = false;
	}

	public Frame findParentFrame() {
		Container c = this;
		while (c != null) {
			if (c instanceof Frame)
				return (Frame) c;

			c = c.getParent();
		}
		return (Frame) null;
	}

	/**
	 * loadCatalog loads the catalog.
	 */
	private void loadCatalog() {
		catalog = new Catalog();
		try {
			String cata = TextLoader.loadText(new URL(getCodeBase(), "scripts/catalog.txt"));

			Vector<AMLBlock> blocks = AMLInturpreter.getBlocks(cata);
			for (int n = 0; n < blocks.size(); n++) {
				AMLBlock block = blocks.get(n);
				if (block.command.equals("Terrain")) {
					catalog.addTerrain(Terrain.parse(block));
				} else if (block.command.equals("Item")) {
					catalog.addItem(Item.parse(block));
				} else if (block.command.equals("Entity")) {
					catalog.addEntity(Entity.parse(block));
				} else if(block.command.equals("Status")) {
					catalog.addStatus(Status.parse(block));
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * loadImages loads the core images and the images specified in the catalog.
	 */

	private void loadImages() {
		//load statuses
		for (int n = 0; n < catalog.statuses.size(); n++) {
			cache.loadImage(catalog.statuses.get(n).src);
		}
		// load terrain images
		for (int n = 0; n < catalog.terrains.size(); n++) {
			cache.loadImage(catalog.terrains.get(n).src);
		}
		// load item images
		for (int n = 0; n < catalog.items.size(); n++) {
			cache.loadImage(catalog.items.get(n).src);
		}
		// load entity images
		for (int n = 0; n < catalog.entities.size(); n++) {
			cache.loadImage(catalog.entities.get(n).src);
			cache.loadImage(catalog.entities.get(n).atksrc);
			cache.loadImage(catalog.entities.get(n).prtsrc);
		}

		// ui
		cache.loadImage("images/ui/buttonback.png");
		cache.loadImage("images/ui/darkbuttonback.png");
		cache.loadImage("images/ui/border.png");
		cache.loadImage("images/ui/entityinfo.png");
		cache.loadImage("images/ui/inventory.png");
		cache.loadImage("images/ui/equipmentslots.png");
		cache.loadImage("images/ui/up.png");
		cache.loadImage("images/ui/down.png");
		// portraits
		cache.loadImage("images/portraits/default.png");
		// icons
		cache.loadImage("images/icons/peace.png");
		cache.loadImage("images/icons/fight.png");
		cache.loadImage("images/icons/bag.png");
		cache.loadImage("images/icons/grab.png");
		cache.loadImage("images/icons/nograb.png");
		cache.loadImage("images/icons/use.png");
		cache.loadImage("images/icons/equip.png");
		cache.loadImage("images/icons/unequip.png");
		cache.loadImage("images/icons/save.png");
		cache.loadImage("images/icons/open.png");
		cache.loadImage("images/icons/buy.png");
		cache.loadImage("images/icons/sell.png");
		cache.loadImage("images/icons/buyall.png");
		cache.loadImage("images/icons/sellall.png");
		// game
		cache.loadImage("images/misc/outofsight.png");
		cache.loadImage("images/misc/arrowleft.png");
		cache.loadImage("images/misc/arrowright.png");
		cache.loadImage("images/misc/arrowup.png");
		cache.loadImage("images/misc/arrowdown.png");
		cache.loadImage("images/effects/blood_1.png");
		cache.loadImage("images/effects/blood_2.png");
		cache.loadImage("images/effects/blood_3.png");
		cache.loadImage("images/effects/blood_4.png");
		// misc
	}

	/**
	 * loadMap loads a given map.
	 * 
	 * @param zone
	 *            is the map that should be loaded.
	 */
	public void loadMap(String zone) {
		String map = "";
		String content = "";
		int hashIndex = zone.indexOf("#");
		if (hashIndex != -1) {
			map = zone.substring(0, hashIndex);
			content = zone.substring(hashIndex + 1);
		} else {
			map = zone;
		}

		// setup the world
		// destroy the old world
		int index = -1;
		if (world != null) {
			index = children.indexOf(world);

			// removeChild(world);
		}
		// make the new one
		world = new World(this, map, catalog, listener);
		world.y = 25;
		world.x = 115;
		if (index == -1) {
			addChild(world);
		} else {
			children.set(index, world);
		}

		// interpreting the map post data
		if (content != "") {
			Vector<AMLBlock> blocks = AMLInturpreter.getBlocks(content);
			for (int n = 0; n < blocks.size(); n++) {
				AMLBlock block = blocks.get(n);
				if (block.command.equals("Item")) {
					ItemInstance it = new ItemInstance(catalog.getItem(block.getParameterString("name")));
					it.parseStatic(block);
					world.addItem(it);
				} else if (block.command.equals("Entity")) {
					String name = block.getParameterString("name", "");
					if (name != "") {
						Entity model = catalog.getEntity(name);
						if (model != null) {
							Entity entity = model.clone();
							entity.parseStatic(block);
							if(entity.dynamic){
								Entity.parseDynamic(entity, block);
							}
							if (world.addEntity(entity) != null) {
								// reads the entity's inventory
								entity.readItems();
								entity.readEquipment();
							}
						}
					}

				} else if (block.command.equals("Player")) {
					Entity playerGuy = new Entity(block.getParameterString("name"),
							block.getParameterString("src"));
					Entity.parseDynamic(playerGuy, block);
					if (world.addEntity(playerGuy) != null) {
						world.player = playerGuy;
						// load the players images
						// #special
						cache.loadImage(world.player.src);
						cache.loadImage(world.player.atksrc);
						cache.loadImage(world.player.prtsrc);
						// reads the players inventory
						world.player.readItems();
						world.player.readEquipment();
						world.centerScreenOn(world.player.x, world.player.y);
					}
				}
			}
		}
	}

	public void openInventory(Entity entity) {
		world.active = false;
		world.visible = false;
		inv.visible = true;
		inv.reset();
		if(entity!=null){
			inv.setStore(entity);
		}
	}

	public URL getBase() {
		return super.getCodeBase();
	}

	public Component getComponent() {
		return this;
	}

	public Image getImage(URI base, String url) {
		try {
			//System.out.println(base.getPath()+url);
			URL u=new URL(base.toURL(), url);
			System.out.println(""+u.getPath());
			//return ImageIO.read(u);
			return null;
		}catch(Exception e){
			return null;
		}
	}
	
	public ImageObserver getObserver() {
		return this;
	}
	
	public ImageCache getCache() {
		return cache;
	}
	
	public ValmarListener getListener() {
		return listener;
	}

	public World getWorld() {
		return world;
	}

	public Inventory getInventory() {
		return inv;
	}
}
