-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 26, 2026 at 03:11 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `wonka`
--

-- --------------------------------------------------------

--
-- Table structure for table `carts`
--

CREATE TABLE `carts` (
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `carts`
--

INSERT INTO `carts` (`user_id`, `product_id`, `quantity`) VALUES
(2, 1, 2),
(2, 4, 1),
(2, 8, 1),
(3, 8, 4);

-- --------------------------------------------------------

--
-- Table structure for table `favorites`
--

CREATE TABLE `favorites` (
  `product_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `favorites`
--

INSERT INTO `favorites` (`product_id`, `user_id`) VALUES
(15, 2),
(14, 2),
(3, 2),
(4, 2),
(6, 3),
(5, 3),
(14, 3),
(4, 3),
(8, 3);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `shipping_address` text NOT NULL,
  `payment_method` varchar(50) NOT NULL,
  `order_date` datetime DEFAULT current_timestamp(),
  `total_amount` decimal(10,2) NOT NULL,
  `status` varchar(50) DEFAULT 'pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `shipping_address`, `payment_method`, `order_date`, `total_amount`, `status`) VALUES
(1, 2, 'Beirut - LAU', 'Cash on Delivery', '2025-11-28 19:40:42', 37.50, 'pending'),
(2, 2, 'Beirut - LAU', 'Cash on Delivery', '2025-11-29 15:35:03', 17.00, 'pending'),
(3, 3, 'Beirut - LAU', 'Cash on Delivery', '2025-11-29 17:47:09', 9.50, 'pending'),
(4, 3, 'Beirut - LAU', 'Cash on Delivery', '2025-11-29 18:21:28', 15.00, 'pending'),
(5, 3, 'Beirut - LAU', 'Cash on Delivery', '2025-11-29 18:23:43', 15.00, 'pending'),
(6, 3, 'Beirut - LAU', 'Cash on Delivery', '2025-11-29 18:28:01', 15.00, 'pending'),
(7, 3, 'Beirut - LAU', 'Cash on Delivery', '2025-11-29 18:30:38', 15.00, 'pending'),
(8, 3, 'Beirut - LAU', 'Cash on Delivery', '2025-11-29 18:40:01', 10.50, 'pending'),
(9, 3, 'Beirut - LAU', 'Cash on Delivery', '2025-11-30 18:24:54', 25.50, 'pending'),
(10, 3, 'Beirut - lau', 'Cash on Delivery', '2025-11-30 18:55:29', 9.50, 'pending'),
(11, 3, 'Beirut - lau', 'Cash on Delivery', '2025-11-30 19:13:50', 14.00, 'pending');

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `quantity`, `price`) VALUES
(5, 10, 2, 1, 5.5),
(6, 10, 15, 1, 4),
(7, 11, 4, 2, 7);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(300) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `img` varchar(300) NOT NULL,
  `img_path` varchar(300) NOT NULL,
  `ingredients` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `price`, `img`, `img_path`, `ingredients`) VALUES
(1, 'Wonka Triple Chocolate Whipple', 6.50, 'wonkatriplechocolatewhipple.jpg', 'uploads/wonkatriplechocolatewhipple.jpg', '[\"Sugar\", \"Cocoa Butter\", \"Whole Milk Powder\", \"Cocoa Mass\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Everlasting Snowflake from Wonka\'s Inventing Room\"]\n'),
(2, 'Wonka Chocolate Tales', 5.50, 'wonkachocolatetales.jpg', 'uploads/wonkachocolatetales.jpg', '[\"Sugar\", \"Cocoa Butter\", \"Whole Milk Powder\", \"Cocoa Mass\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Cocoa Dust from Willy Wonka\'s Chocolate River\"]\n'),
(3, 'Wonka Nutty Crunchilicious', 6.00, 'wonkanuttycrunchilicious.jpg', 'uploads/wonkanuttycrunchilicious.jpg', '[\"Sugar\", \"Cocoa Butter\", \"Cocoa Mass\", \"Whole Milk Powder\", \"Roasted Hazelnuts\", \"Caramelized Nut Crunch Pieces\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Golden Nut Sparkles from the Squirrel Sorting Room\"]\r\n'),
(4, 'Wonka Caramel Hat Trick', 7.00, 'wonkacaramelhattrick.jpg', 'uploads/wonkacaramelhattrick.jpg', '[\"Sugar\", \"Cocoa Butter\", \"Cocoa Mass\", \"Whole Milk Powder\", \"Silky Caramel Filling\", \"Caramel Crunch Pieces\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Magical Hat-Tip Caramel pulled from Willy Wonka\'s Purple Hat\"]\r\n'),
(5, 'Wonka Nutty Crunch Surprise', 4.00, 'wonkanuttycrunchsurprise.png', 'uploads/wonkanuttycrunchsurprise.png', '[\"Sugar\", \"Cocoa Butter\", \"Cocoa Mass\", \"Whole Milk Powder\", \"Crunchy Nut Clusters\", \"Roasted Peanuts\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Surprise Nut Crystals cracked by Wonka\'s Trained Squirrels\"]\r\n'),
(6, 'Wonka Triple Dazzle Caramel', 4.00, 'wonkatripledazzlecaramel.png', 'uploads/wonkatripledazzlecaramel.png', '[\"Sugar\", \"Cocoa Butter\", \"Cocoa Mass\", \"Whole Milk Powder\", \"Layered Caramel Filling\", \"Caramel Toffee Bits\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Dazzle Drops spun from the Chocolate River\'s Caramel Waterfall\"]\n'),
(7, 'Wonka Whipple Scrumptious Fudgemallow Delight', 4.00, 'wonkawhipplescrumptiousfudgemallowdelight.jpg', 'uploads/wonkawhipplescrumptiousfudgemallowdelight.jpg', '[\"Sugar\", \"Cocoa Butter\", \"Cocoa Mass\", \"Whole Milk Powder\", \"Fudge Filling\", \"Marshmallow Swirl\", \"Chocolate Cookie Pieces\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Fudgemallow Fluff harvested from Willy Wonka\'s Inventing Room\"]'),
(8, 'Wonka Chilly Chocolate Creme', 4.00, 'wonkachillychocolatecreme.png', 'uploads/wonkachillychocolatecreme.png', '[\"Sugar\", \"Cocoa Butter\", \"Cocoa Mass\", \"Whole Milk Powder\", \"Chilled Chocolate Creme Filling\", \"Frosted Chocolate Shards\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Everfrost Essence from Wonka\'s Ice Cream Machine\"]'),
(9, 'Wonka Bar', 2.50, 'wonkabar.JPG', 'uploads/wonkabar.JPG', '[\"Sugar\", \"Cocoa Butter\", \"Whole Milk Powder\", \"Cocoa Mass\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Wonka\'s Tears of Joy\"]'),
(10, 'Wonka Crème Brûlée', 5.00, 'wonkacremebrulee.JPG', 'uploads/wonkacremebrulee.JPG', '[\"Sugar\", \"Cocoa Butter\", \"Whole Milk Powder\", \"Cocoa Mass\", \"Silky Crème Brûlée Filling\", \"Caramelized Sugar Bits\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Golden Caramel Flakes from Wonka\'s Inventing Room\"]'),
(11, 'Wonka Milk Chocolate Bar with Graham Crackers', 5.50, 'wonkamilkchocolatebarwithgrahamcrackers.WEBP', 'uploads/wonkamilkchocolatebarwithgrahamcrackers.WEBP', '[\"Sugar\", \"Cocoa Butter\", \"Whole Milk Powder\", \"Cocoa Mass\", \"Graham Cracker Pieces\", \"Milk Chocolate Chunks\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Crunchy Crumbs from Willy Wonka\'s Chocolate Factory\"]'),
(12, 'Wonka Chocolate Nice Cream', 4.25, 'wonkachocolatenicecream.jpg', 'uploads/wonkachocolatenicecream.jpg', '[\"Sugar\", \"Cocoa Butter\", \"Whole Milk Powder\", \"Cocoa Mass\", \"Creamy Chocolate Filling\", \"Frozen Chocolate Swirls\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Magical Chill Drops from Willy Wonka\'s Ice Cream Lab\"]'),
(13, 'Wonka Donutz', 3.50, 'wonkadonutz.PNG', 'uploads/wonkadonutz.PNG', '[\"Sugar\", \"Cocoa Butter\", \"Whole Milk Powder\", \"Cocoa Mass\", \"Soft Donut Pieces\", \"Chocolate Glaze\", \"Sprinkle Crunchies\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Oompa Loompa Glimmer Dust from Wonka\'s Kitchen\"]'),
(14, 'Wonka Fudgemallow', 5.25, 'wonkafudgemallow.png', 'uploads/wonkafudgemallow.png', '[\"Sugar\", \"Cocoa Butter\", \"Whole Milk Powder\", \"Cocoa Mass\", \"Fudge Filling\", \"Marshmallow Swirl\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Enchanted Fudgemallow Fluff from Willy Wonka\'s Inventing Room\"]'),
(15, 'Wonka Square Chocolates', 4.00, 'wonkasquarechocolates.png', 'uploads/wonkasquarechocolates.png', '[\"Sugar\", \"Cocoa Butter\", \"Whole Milk Powder\", \"Cocoa Mass\", \"Chocolate Ganache Filling\", \"Crispy Chocolate Bits\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Magical Chocolate Essence from Willy Wonka\'s Chocolate River\"]'),
(16, 'Wonka Scrumdiddlyumptious', 6.50, 'wonkascrumdiddlyumptious.png', 'uploads/wonkascrumdiddlyumptious.png', '[\"Sugar\", \"Cocoa Butter\", \"Whole Milk Powder\", \"Cocoa Mass\", \"Fudge Swirls\", \"Caramel Crunch Bits\", \"Marshmallow Pieces\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Scrumdiddlyumptious Essence from Willy Wonka\'s Inventing Room\"]'),
(17, 'Wonka Chocolate Waterfall', 7.00, 'wonkachocolatewaterfall.jpg', 'uploads/wonkachocolatewaterfall.jpg', '[\"Sugar\", \"Cocoa Butter\", \"Whole Milk Powder\", \"Cocoa Mass\", \"Chocolate Fudge Flow\", \"Chocolate Crisps\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Liquid Chocolate Drops from Willy Wonka\'s Chocolate River\"]'),
(18, 'Wonka Domed Dark Chocolate', 6.50, 'wonkadomeddarkchocolate.png', 'uploads/wonkadomeddarkchocolate.png', '[\"Sugar\", \"Cocoa Mass\", \"Cocoa Butter\", \"Whole Milk Powder\", \"Dark Chocolate Ganache Filling\", \"Chocolate Crunch Pieces\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Midnight Cocoa Essence from Willy Wonka\'s Inventing Room\"]'),
(19, 'Wonka Triple Dazzle Caramel', 7.50, 'wonkatripledazzlecaramel.jpg', 'uploads/wonkatripledazzlecaramel.jpg', '[\"Sugar\", \"Cocoa Butter\", \"Cocoa Mass\", \"Whole Milk Powder\", \"Layered Caramel Filling\", \"Caramel Toffee Bits\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Dazzle Drops spun from the Chocolate River\'s Caramel Waterfall\"]'),
(20, 'Wonka Fantabulous Fudge', 8.00, 'wonkafantabulousfudge.jpg', 'uploads/wonkafantabulousfudge.jpg', '[\"Sugar\", \"Cocoa Butter\", \"Cocoa Mass\", \"Whole Milk Powder\", \"Rich Fudge Filling\", \"Chocolate Crunch Pieces\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Fantabulous Fudge Essence from Willy Wonka\'s Inventing Room\"]'),
(21, 'Wonka Scrumptilicious Milky Chocolate Bar', 5.50, 'wonkascrumptiliciousmilkychocolatebar.webp', 'uploads/wonkascrumptiliciousmilkychocolatebar.webp', '[\"Sugar\", \"Cocoa Butter\", \"Chocolate\", \"Nonfat Milk\", \"Peanuts\", \"Milk Fat\", \"Lactose\", \"Butter\", \"Rice Flour\", \"Palm Oil\", \"Soy Lecithin (as an emulsifier)\", \"Corn Flour\", \"Modified Corn Starch\", \"Salt\", \"Natural Flavors\", \"High Fructose Corn Syrup\", \"Scrumptilicious Cocoa Essence from Willy Wonka\'s Inventing Room\"]'),
(22, 'Wonka Scrumdiddlyumptious Milky Chocolate Bar', 4.50, 'wonkascrumdiddlyumptiousmilkychocolatebar.png', 'uploads/wonkascrumdiddlyumptiousmilkychocolatebar.png', '[\"Sugar\", \"Cocoa Butter\", \"Whole Milk Powder\", \"Cocoa Mass\", \"Milky Chocolate Filling\", \"Caramel Swirls\", \"Crunchy Chocolate Bits\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Scrumdidlyumptious Essence from Willy Wonka\'s Chocolate Factory\"]'),
(23, 'Wonka Triple Dazzle Caramel Pieces', 10.00, 'wonkatripledazzlecaramelpieces.png', 'uploads/wonkatripledazzlecaramelpieces.png', '[\"Sugar\", \"Cocoa Butter\", \"Cocoa Mass\", \"Whole Milk Powder\", \"Layered Caramel Filling\", \"Caramel Toffee Bits\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Dazzle Drops spun from the Chocolate River\'s Caramel Waterfall\"]'),
(24, 'Mini Wonka Bar', 3.50, 'miniwonkabars.png', 'uploads/miniwonkabars.png', '[\"Sugar\", \"Cocoa Butter\", \"Whole Milk Powder\", \"Cocoa Mass\", \"Soy Lecithin\", \"Natural Vanilla Flavoring\", \"Wonka\'s Tears of Joy\"]'),
(25, 'Wonka Oompas', 6.00, 'wonkaoompas.WEBP', 'uploads/wonkaoompas.WEBP', '[\"Sugar\", \"Dextrose\", \"Corn Syrup\", \"Citric Acid\", \"Artificial Fruit Flavors\", \"Artificial Colors(Red 40 Lake, Blue 1, Yellow 5)\", \"Modified Corn Starch\", \"Loompaland Fruit Essence gathered from Wonka\'s secret candy orchards\"]'),
(26, 'Wonka Poppin Pops', 8.00, 'wonkapoppinpops.png', 'uploads/wonkapoppinpops.png', '[\"Water\", \"Sugar\", \"Dextrose\", \"Corn Syrup\", \"Soybean Oil\", \"Natural Fruit Flavors\", \"Beet Juice Concentrate Color\", \"Annatto Color\", \"Carbonated Crystals (Sugar, Lactose, Corn Syrup, Carbon Dioxide)\", \"Guar Gum\", \"Soy Lecithin\", \"Lactic Acid\", \"Pop-Spark Essence from Willy Wonka\'s Inventing Room\"]'),
(27, 'Wonka Tart N Tinys', 7.50, 'wonkatartntinys.JPG', 'uploads/wonkatartntinys.JPG', '[\"Dextrose with Maltodextrin\", \"Dextrose\", \"Malic Acid\", \"Citric Acid\", \"Natural and Artificial Flavors\", \"Stearic Acid\", \"Magnesium Stearate\", \"Artificial Colors (Blue 1 Lake, Yellow 6 Lake, Yellow 5 Lake, and Red 40 Lake)\", \"Tiny Tingle Dust from Willy Wonka\'s Candy Lab\"]'),
(28, 'Wonka Magic Hat Gummies', 9.00, 'wonkamagichatgummies.JPG', 'uploads/wonkamagichatgummies.JPG', '[\"Corn Syrup\", \"Sugar\", \"Gelatin\", \"Water\", \"Contains 2% Or Less Of Citric Acid\", \"Malic Acid (DL-Malic Acid)\", \"Pectin\", \"Fruit And Vegetable Juice (Color)\", \"Coconut Oil\", \"Natural And Artificial Flavors\", \"Artificial Colors (Red 40, Yellow 5, Blue 1)\", \"Sodium Citrate\", \"Carnauba Wax\", \"Enchanted Hat-Tip Flavor lifted from Willy Wonka\'s Purple Hat\"]'),
(29, 'Wonka Everlasting Gobstoppers', 8.50, 'wonkaeverlastinggobstoppers.png', 'uploads/wonkaeverlastinggobstoppers.png', '[\"Dextrose\", \"Corn Syrup\", \"Less Than 2% Of Maltodextrin\", \"Hydrogenated Coconut Oil\", \"Malic Acid\", \"Calcium Stearate\", \"Egg Albumen\", \"Artificial Colors (Blue 1, Blue 2 Lake, Red 40 Lake, Yellow 5 Lake, Yellow 6 Lake)\", \"Sunflower Lecithin\", \"Mineral Oil\", \"Layered Candy Coating\", \"Everlasting Core Essence from Willy Wonka\'s Inventing Room\"]'),
(30, 'Wonka Bottle Caps', 5.50, 'wonkabottlecaps.JPG', 'uploads/wonkabottlecaps.JPG', '[\"Dextrose\", \"Maltodextrin\", \"Less Than 2% Of Calcium Stearate\", \"Malic Acid\", \"Natural Flavors\", \"Annatto Extract Color\", \"Caramel Color\", \"Vegetable Juice Color\", \"Colors (Blue 1 Lake, Blue 2 Lake, Red 40 Lake, Yellow 5 Lake, Yellow 6 Lake)\", \"May Contain Egg\", \"Sparkling Soda Fizz From Willy Wonka’s Bubble Room\"\r\n]'),
(31, 'Wonka Runts', 6.50, 'wonkarunts.WEBP', 'uploads/wonkarunts.WEBP', '[\"Dextrose\", \"Maltodextrin\", \"2% Or Less Of Malic Acid\", \"Calcium Stearate\", \"Corn Syrup\", \"Natural Flavors\", \"Carnauba Wax\", \"Carmine Color\", \"Colors (Blue 1, Blue 1 Lake, Blue 2 Lake, Red 40 Lake, Yellow 5, Yellow 5 Lake, Yellow 6)\", \"Whispering Fruit Essence Gathered From Willy Wonka\'s Enchanted Orchard\"\r\n]'),
(32, 'Wonka Nerds', 8.00, 'wonkanerds.PNG', 'uploads/wonkanerds.PNG', '[\"Dextrose\", \"Sugar\", \"Malic Acid\", \"Less Than 2% Of Corn Syrup\", \"Natural Flavors\", \"Carnauba Wax\", \"Carmine Color\", \"Colors (Blue 1, Blue 1 Lake, Blue 2 Lake, Red 40 Lake, Yellow 5, Yellow 5 Lake, Yellow 6)\",  \"Rainbow Fruit Sparkles From Willy Wonka\'s Candy Lab\"\r\n]'),
(33, 'Wonka Laffy Taffy', 5.50, 'wonkalaffytaffy.jpg', 'uploads/wonkalaffytaffy.jpg', '[\"Corn Syrup\", \"Sugar\", \"Palm Oil\", \"Hydrogenated Oils\", \"Malic Acid\", \"Mono- And Diglycerides\", \"Soy Lecithin\", \"Natural And Artificial Flavors\", \"Colors (Red 40, Yellow 5, Blue 1)\", \"Egg Whites\", \"Sodium Alginate\", \"Calcium Acetate\", \"Salt\", \"Confectioner\'s Glaze\", \"Carnauba Wax\", \"Titanium Dioxide\", \"Laffy Loompa Laugh Dust from Willy Wonka\'s Factory\"]'),
(34, 'Wonka Laffy Taffy Laff Bites', 7.50, 'wonkalaffytaffylaffbites.webp', 'uploads/wonkalaffytaffylaffbites.webp', '[\"Corn Syrup\", \"Sugar\", \"Palm Oil\", \"Hydrogenated Oils\", \"Malic Acid\", \"Mono- And Diglycerides\", \"Soy Lecithin\", \"Natural And Artificial Flavors\", \"Colors (Red 40, Yellow 5, Blue 1)\", \"Egg Whites\", \"Sodium Alginate\", \"Calcium Acetate\", \"Salt\", \"Confectioner\'s Glaze\", \"Carnauba Wax\", \"Titanium Dioxide\", \"Laffy Loompa Laugh Dust from Willy Wonka\'s Factory\"]'),
(35, 'Wonka SweetTarts', 5.25, 'wonkasweettarts.webp', 'uploads/wonkasweettarts.webp', '[\"Dextrose\", \"Maltodextrin\", \"Malic Acid\", \"Less Than 2% Of Calcium Stearate\", \"Natural Flavors\", \"Colors (Blue 1 Lake, Blue 2 Lake, Red 40 Lake, Yellow 5 Lake)\", \"Fizzing SweetSpark Essence from Willy Wonka\'s Candy Lab\"]\r\n'),
(36, 'Wonka Pixy Stix', 4.50, 'wonkapixystix.webp', 'uploads/wonkapixystix.webp', '[\"Dextrose\", \"Maltodextrin\", \"Citric Acid\", \"Less Than 2% Of Natural Flavors\", \"Colors (Blue 1 Lake, Blue 2 Lake, Red 40 Lake, Yellow 5 Lake, Yellow 6 Lake)\", \"Pixy Dust Sparkle from Willy Wonka\'s Inventing Room\"]\r\n');

-- --------------------------------------------------------

--
-- Table structure for table `tickets`
--

CREATE TABLE `tickets` (
  `code` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tickets`
--

INSERT INTO `tickets` (`code`) VALUES
('A5H3ILO3'),
('B7K1PQ9Z'),
('C2M8XR4T'),
('D6V0LS7Q'),
('E9F2YW1J'),
('F3N5KU8P'),
('G8Q6OM2L'),
('H1R7TJ5S');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `profile_pic` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `phone`, `password`, `profile_pic`) VALUES
(1, 'Dana', 'dana.itani05@hotmail.com', '71121195', '$2y$10$FgcCLliJ5F.TwwPgpYGspOEcKRDTVwGQnIyIUnexfi3A3B7OMNB4e', NULL),
(2, 'testd', 'test@hotmail.com', '87654123', '$2y$10$9xcpI3ELiO2SDuVx9wiC2el23Yo6jhceaiDBAyk6MGL8KjprtBmiO', NULL),
(3, 'test', 'test@hotmail.com', '71121111', '$2y$10$A3bhlvzP5cbjx7tj5NnSBO6meWx1Mnzu0kOiv23yQ34fOpb6OheDa', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `carts`
--
ALTER TABLE `carts`
  ADD PRIMARY KEY (`user_id`,`product_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `favorites`
--
ALTER TABLE `favorites`
  ADD KEY `product_id` (`product_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `fk_orderitems_product` (`product_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tickets`
--
ALTER TABLE `tickets`
  ADD PRIMARY KEY (`code`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `carts`
--
ALTER TABLE `carts`
  ADD CONSTRAINT `carts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `carts_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `fk_orderitems_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
