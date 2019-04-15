import java.io.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;
import java.util.Map.*;
import java.util.function.*;
import java.util.stream.*;

public class CodeGo2019 {

    static final String SEMICOLON = ";";
    static final String COLON = ",";
    static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
    static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.000");

    static final Float EXPERIENCE_PRICE_BY_HOUR = 0.03f;

    static enum Warehouse {
        NEW_YORK(-4),
        SAN_FRANCISCO(-7);

        private Integer timeZoneOffset;

        Warehouse(int timeZoneOffset) {
            this.timeZoneOffset = timeZoneOffset;
        }

        static Warehouse fromName(String input) {
            switch (input) {
                case "New York":
                    return NEW_YORK;
                case "San Francisco":
                    return SAN_FRANCISCO;
            }
            return null;
        }

        public String toName() {
            switch (this) {
                case NEW_YORK:
                    return "New York";
                case SAN_FRANCISCO:
                    return "San Francisco";
            }
            return null;
        }

        public Integer getTimeZoneOffset() {
            return timeZoneOffset;
        }
    }

    static class Stock {
        final String itemId;
        final Warehouse warehouse;
        final int stock;

        Stock(String itemId, Warehouse warehouse, int stock) {
            this.itemId = itemId;
            this.warehouse = warehouse;
            this.stock = stock;
        }

        public String getItemId() {
            return itemId;
        }

        public Warehouse getWarehouse() {
            return warehouse;
        }

        public int getStock() {
            return stock;
        }

    }

    static class BoxType {
        final String boxType;
        final int maxWeight;
        final int length, width, height; // cm
        final float volume; // dm3

        BoxType(String boxType, int maxWeight, int length, int width, int height, float volume) {
            this.boxType = boxType;
            this.maxWeight = maxWeight;
            this.length = length;
            this.width = width;
            this.height = height;
            this.volume = volume;
        }

        public String getBoxType() {
            return boxType;
        }

        public int getMaxWeight() {
            return maxWeight;
        }

        public int getLength() {
            return length;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public float getVolume() {
            return volume;
        }

    }

    static class CarrierPricing {
        final Warehouse warehouse;
        final String targetState;
        final float volumePrice; // $/dm3

        CarrierPricing(Warehouse warehouse, String targetState, float volumePrice) {
            this.warehouse = warehouse;
            this.targetState = targetState;
            this.volumePrice = volumePrice;
        }

        public Warehouse getWarehouse() {
            return warehouse;
        }

        public String getTargetState() {
            return targetState;
        }

        public float getVolumePrice() {
            return volumePrice;
        }

    }

    static class ShippingHour {
        final DayOfWeek day;
        final LocalTime time;

        ShippingHour(DayOfWeek day, LocalTime time) {
            this.time = time;
            this.day = day;
        }

        public DayOfWeek getDay() {
            return day;
        }

        public LocalTime getTime() {
            return time;
        }

    }

    static class DepartureTime {
        final Warehouse warehouse;
        final String targetState;
        final List<ShippingHour> shippingHours;

        DepartureTime(Warehouse warehouse, String targetState, List<ShippingHour> shippingHours) {
            this.warehouse = warehouse;
            this.targetState = targetState;
            this.shippingHours = shippingHours;
        }

        public Warehouse getWarehouse() {
            return warehouse;
        }

        public String getTargetState() {
            return targetState;
        }

        public List<ShippingHour> getShippingHours() {
            return shippingHours;
        }

    }

    static class CarrierTime {
        final Warehouse warehouse;
        final String targetState;
        final int carrierTime; // in hours

        CarrierTime(Warehouse warehouse, String targetState, int carrierTime) {
            this.warehouse = warehouse;
            this.targetState = targetState;
            this.carrierTime = carrierTime;
        }

        public Warehouse getWarehouse() {
            return warehouse;
        }

        public String getTargetState() {
            return targetState;
        }

        public int getCarrierTime() {
            return carrierTime;
        }

    }

    static class Item {
        final String itemId;
        final int weight;
        final int length, width, height;

        Item(String itemId, int weight, int length, int width, int height) {
            this.itemId = itemId;
            this.weight = weight;
            this.length = length;
            this.width = width;
            this.height = height;
        }

        public String getItemId() {
            return itemId;
        }

        public int getLength() {
            return length;
        }

        public int getWeight() {
            return weight;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

    }

    static class Order {
        final long orderId;
        final LocalDateTime orderDate;
        final String itemId;
        final String targetState;

        Order(long orderId, LocalDateTime orderDate, String itemId, String targetState) {
            this.orderId = orderId;
            this.itemId = itemId;
            this.orderDate = orderDate;
            this.targetState = targetState;
        }

        public long getOrderId() {
            return orderId;
        }

        public LocalDateTime getOrderDate() {
            return orderDate;
        }

        public String getItemId() {
            return itemId;
        }

        public String getTargetState() {
            return targetState;
        }

    }

    static class ShipmentInfo {
        final Order order;
        final Warehouse warehouse;
        final LocalDateTime guaranteedDeliveryDate;
        final String boxType;
        final float shippingPrice;

        ShipmentInfo(Order order, Warehouse warehouse, LocalDateTime guaranteedDeliveryDate, String boxType,
                     float shippingPrice) {
            this.order = order;
            this.warehouse = warehouse;
            this.guaranteedDeliveryDate = guaranteedDeliveryDate;
            this.boxType = boxType;
            this.shippingPrice = shippingPrice;
        }

        public Order getOrder() {
            return order;
        }

        public String getItemId() {
            return order.itemId;
        }

        public Warehouse getWarehouse() {
            return warehouse;
        }

        public LocalDateTime getGuaranteedDeliveryDate() {
            return guaranteedDeliveryDate;
        }

        public String getBoxType() {
            return boxType;
        }

        public float getShippingPrice() {
            return shippingPrice;
        }

        public String toCsvLine() {
            return new StringBuilder().append(order.orderId).append(SEMICOLON).append(warehouse.toName())
                    .append(SEMICOLON).append(DATE_PATTERN.format(guaranteedDeliveryDate)).append(SEMICOLON)
                    .append(boxType).append(SEMICOLON).append(DECIMAL_FORMAT.format(shippingPrice)).append(SEMICOLON)
                    .append(DECIMAL_FORMAT.format(getShippingExperiencePrice())).toString();
        }

        public Float getShippingExperiencePrice() {
            long hours = order.orderDate.until(guaranteedDeliveryDate, ChronoUnit.HOURS);
            return hours * EXPERIENCE_PRICE_BY_HOUR;
        }

        public Float getTotalPrice() {
            return getShippingPrice() + getShippingExperiencePrice();
        }

    }

    static class CsvParser {

        public static final Order parseOrder(String inputLine) {
            String[] input = inputLine.split(SEMICOLON);
            LocalDateTime orderDate = LocalDateTime.parse(input[1], DATE_PATTERN);
            return new Order(Long.valueOf(input[0]), orderDate, input[2], input[4]);
        }

        public static final Stock parseStock(String inputLine) {
            String[] input = inputLine.split(SEMICOLON);
            return new Stock(input[0], Warehouse.fromName(input[1]), Integer.valueOf(input[2]));
        }

        public static final BoxType parseBoxType(String inputLine) {
            String[] input = inputLine.split(SEMICOLON);
            return new BoxType(input[0], Integer.valueOf(input[1]), Integer.valueOf(input[2]),
                    Integer.valueOf(input[3]), Integer.valueOf(input[4]), Float.valueOf(input[5].replaceAll(",", ".")));
        }

        public static final CarrierPricing parseCarrierPricings(String inputLine) {
            String[] input = inputLine.split(SEMICOLON);
            String costString = input[2];
            return new CarrierPricing(Warehouse.fromName(input[0]), input[1],
                    Float.valueOf(costString.replaceAll(",", ".")));
        }

        public static final DepartureTime parseDepartureTime(String inputLine) {
            String[] input = inputLine.split(SEMICOLON);
            Warehouse warehouse = Warehouse.fromName(input[0]);

            String departureTimes[] = input[2].split(COLON);
            List<ShippingHour> shippingHours = Arrays.stream(departureTimes).map(new Function<String, ShippingHour>() {

                @Override
                public ShippingHour apply(String departureTime) {
                    String[] values = departureTime.trim().split(" ");
                    DayOfWeek dayOfWeek = DayOfWeek.valueOf(values[0]);
                    LocalTime localTime = LocalTime.parse(values[1]);
                    return new ShippingHour(dayOfWeek, localTime);
                }
            }).collect(Collectors.toList());
            return new DepartureTime(warehouse, input[1], shippingHours);
        }

        public static final CarrierTime parseCarrierTime(String inputLine) {
            String[] input = inputLine.split(SEMICOLON);
            return new CarrierTime(Warehouse.fromName(input[0]), input[1], Integer.valueOf(input[2].split(" ")[0]));
        }

        public static final Item parseItem(String inputLine) {
            String[] input = inputLine.split(SEMICOLON);
            return new Item(input[0], Integer.valueOf(input[2]), Integer.valueOf(input[3]), Integer.valueOf(input[4]),
                    Integer.valueOf(input[5]));
        }
    }

    static class ShipmentsManager {

        final Integer PACKAGE_PREPARATION_HOURS = 4;

        static class NoSuitableBoxException extends RuntimeException {
            private static final long serialVersionUID = 7513400494522133911L;

            public NoSuitableBoxException(String itemId) {
                super("There is no suitable for item " + itemId);
            }
        }

        static class NoSuitableWarehouseException extends RuntimeException {
            private static final long serialVersionUID = 7513400494522133911L;

            public NoSuitableWarehouseException(String itemId, String targetState) {
                super("There is no warehouse with stock and deliver options for item " + itemId + " and target state "
                        + targetState);
            }
        }

        private final CarrierPricingRepository carrierPricingRepository;
        private final BoxTypeRepository boxTypeRepository;
        private final ItemRepository itemRepository;
        private final DepartureTimeRepository departureTimeRepository;
        private final CarrierTimeRepository carrierTimeRepository;
        private final StockRepository stockRepository;

        public ShipmentsManager(List<Item> items, List<BoxType> boxTypes, List<CarrierPricing> carrierPricings,
                                List<DepartureTime> departureTimes, List<CarrierTime> carrierTimes, List<Stock> initialStocks) {
            carrierPricingRepository = new CarrierPricingRepository(carrierPricings);
            boxTypeRepository = new BoxTypeRepository(boxTypes);
            itemRepository = new ItemRepository(items);
            departureTimeRepository = new DepartureTimeRepository(departureTimes);
            carrierTimeRepository = new CarrierTimeRepository(carrierTimes);
            stockRepository = new StockRepository(initialStocks);
        }

        public ShipmentInfo findBestShipmentInfo(Order order) {
            ShipmentInfo shipmentInfo = stockRepository.getStocks().stream().filter(stock ->
                    stock.getItemId().equals(order.getItemId()) && stock.getStock() > 0
            ).map(stock -> {
                Item item = itemRepository.findById(order.getItemId()).orElseThrow(IllegalStateException::new);
                BoxType boxType = boxTypeRepository.findByItem(item);
                CarrierPricing carrierPricing = carrierPricingRepository
                        .findByWarehouseAndState(stock.getWarehouse(), order.getTargetState())
                        .orElseThrow(IllegalStateException::new);
                Float carrierPrice = carrierPricing.getVolumePrice() * boxType.getVolume();

                DepartureTime departureTime = departureTimeRepository
                        .findByWarehouseAndState(stock.getWarehouse(), order.getTargetState())
                        .orElseThrow(IllegalStateException::new);
                LocalDateTime preparedPackageInWarehouseTimeZoneDate = order.getOrderDate()
                        .plusHours(PACKAGE_PREPARATION_HOURS)
                        .plusHours(stock.getWarehouse().getTimeZoneOffset());
                LocalDateTime departureDate = departureTime.getShippingHours().stream()
                        .map(shippingHour ->
                                ((preparedPackageInWarehouseTimeZoneDate.getHour() < shippingHour.getTime().getHour()) ?
                                        preparedPackageInWarehouseTimeZoneDate.with(TemporalAdjusters.nextOrSame(shippingHour.getDay())) :
                                        preparedPackageInWarehouseTimeZoneDate.with(TemporalAdjusters.next(shippingHour.getDay())))
                                        .with(LocalTime.of(shippingHour.getTime().getHour(), shippingHour.getTime().getMinute()))
                                        .minusHours(stock.getWarehouse().getTimeZoneOffset()))
                        .min(LocalDateTime::compareTo)
                        .orElseThrow(IllegalStateException::new);
                CarrierTime carrierTime = carrierTimeRepository
                        .findByWarehouseAndState(stock.getWarehouse(), order.getTargetState())
                        .orElseThrow(IllegalStateException::new);
                LocalDateTime guaranteedDeliveryDate = departureDate.plusHours(carrierTime.getCarrierTime());
                return new ShipmentInfo(order, stock.warehouse, guaranteedDeliveryDate, boxType.getBoxType(), carrierPrice);
            }).min((si1, si2) -> {
                int compareTotalPrice = Float.compare(
                        si1.shippingPrice + si1.getShippingExperiencePrice(),
                        si2.shippingPrice + si2.getShippingExperiencePrice());
                return (compareTotalPrice != 0) ? compareTotalPrice :
                        Integer.compare(
                                stockRepository.findByItemIdAndWareHouse(
                                        si2.getItemId(),
                                        si2.getWarehouse())
                                        .orElseThrow(IllegalStateException::new)
                                        .getStock(),
                                stockRepository.findByItemIdAndWareHouse(
                                        si1.getItemId(),
                                        si1.getWarehouse())
                                        .orElseThrow(IllegalStateException::new)
                                        .getStock());
            }).orElseThrow(() -> new NoSuitableWarehouseException(order.getItemId(), order.getTargetState()));
            stockRepository.reduceStockByItemIdAndWareHouse(shipmentInfo.getItemId(), shipmentInfo.getWarehouse());
            return shipmentInfo;
        }

        public List<ShipmentInfo> findBestShipmentInfo(List<Order> orders) {
            orders.stream()
            return new ArrayList<ShipmentInfo>();
        }

        static class CarrierPricingRepository {
            private final List<CarrierPricing> carrierPricings;

            CarrierPricingRepository(List<CarrierPricing> carrierPricings) {
                this.carrierPricings = carrierPricings;
            }

            Optional<CarrierPricing> findByWarehouseAndState(Warehouse warehouse, String state) {
                return carrierPricings.stream()
                        .filter(cp -> cp.getWarehouse().equals(warehouse) && cp.getTargetState().equals(state))
                        .reduce((cp1, cp2) -> {
                            throw new IllegalStateException("Multiple carrier prices between warehouse and state: " + cp1 + ", " + cp2);
                        });
            }
        }

        static class ItemRepository {
            private final List<Item> items;

            ItemRepository(List<Item> items) {
                this.items = items;
            }

            Optional<Item> findById(String itemId) {
                return items.stream()
                        .filter(it -> it.getItemId().equals(itemId))
                        .reduce((it1, it2) -> {
                            throw new IllegalStateException("Multiple items for the same itemId: " + it1 + ", " + it2);
                        });
            }
        }

        static class BoxTypeRepository {
            private final List<BoxType> boxTypes;

            BoxTypeRepository(List<BoxType> boxTypes) {
                this.boxTypes = boxTypes;
            }

            BoxType findByItem(Item item) {
                return boxTypes.stream().filter(bt ->
                        bt.getHeight() >= item.getHeight() &&
                                ((bt.getLength() >= item.getLength() && bt.getWidth() >= item.getWidth()) ||
                                        (bt.getLength() >= item.getWidth() && bt.getWidth() >= item.getLength())) &&
                                bt.getMaxWeight() >= item.getWeight())
                        .min(Comparator.comparing(BoxType::getVolume))
                        .orElseThrow(() -> new NoSuitableBoxException(item.getItemId()));
            }
        }

        static class DepartureTimeRepository {
            private final List<DepartureTime> departureTimes;

            DepartureTimeRepository(List<DepartureTime> departureTimes) {
                this.departureTimes = departureTimes;
            }

            Optional<DepartureTime> findByWarehouseAndState(Warehouse warehouse, String state) {
                return departureTimes.stream()
                        .filter(departureTime ->
                                departureTime.getWarehouse().equals(warehouse) && departureTime.getTargetState().equals(state))
                        .reduce((dp1, dp2) -> {
                            throw new IllegalStateException("Multiple departure times between warehouse and state: " + dp1 + ", " + dp2);
                        });
            }
        }

        static class CarrierTimeRepository {
            private final List<CarrierTime> carrierTimes;

            CarrierTimeRepository(List<CarrierTime> carrierTimes) {
                this.carrierTimes = carrierTimes;
            }

            Optional<CarrierTime> findByWarehouseAndState(Warehouse warehouse, String state) {
                return carrierTimes.stream()
                        .filter(ct ->
                                ct.getWarehouse().equals(warehouse) && ct.getTargetState().equals(state))
                        .reduce((ct1, ct2) -> {
                            throw new IllegalStateException("Multiple carrier times between warehouse and state: " + ct1 + ", " + ct2);
                        });
            }
        }

        static class StockRepository {
            private final List<Stock> stocks;

            StockRepository(List<Stock> stocks) {
                this.stocks = stocks;
            }

            List<Stock> getStocks() {
                return stocks;
            }

            Optional<Stock> findByItemIdAndWareHouse(String itemId, Warehouse warehouse) {
                return stocks.stream()
                        .filter(st ->
                                st.getWarehouse().equals(warehouse) && st.getItemId().equals(itemId))
                        .reduce((stock1, stock2) -> {
                            throw new IllegalStateException("Multiple carrier times between warehouse and state: " + stock1 + ", " + stock2);
                        });
            }

            Stock reduceStockByItemIdAndWareHouse(String itemId, Warehouse warehouse) {
                return IntStream.range(0, stocks.size())
                        .filter(i -> stocks.get(i).getItemId().equals(itemId) && stocks.get(i).getWarehouse().equals(warehouse))
                        .mapToObj(i -> {
                            Stock stock = stocks.get(i);
                            stock = new Stock(stock.getItemId(), stock.getWarehouse(), stock.getStock() - 1);
                            return stocks.set(i, stock);
                        }).findFirst().orElseThrow(IllegalStateException::new);
            }
        }


    }

    public static void main(String[] args) throws IOException {
        List<Stock> stocks = new ArrayList<>();
        List<BoxType> boxTypes = new ArrayList<>();
        List<CarrierPricing> carrierPricings = new ArrayList<>();
        List<DepartureTime> departureTimes = new ArrayList<>();
        List<CarrierTime> carrierTimes = new ArrayList<>();
        List<Item> items = new ArrayList<>();

        List<Order> orders = new ArrayList<>();

        Consumer<String> stockConsumer = input -> stocks.add(CsvParser.parseStock(input));
        Consumer<String> boxTypeConsumer = input -> boxTypes.add(CsvParser.parseBoxType(input));
        Consumer<String> carrierPricingConsumer = input -> carrierPricings.add(CsvParser.parseCarrierPricings(input));
        Consumer<String> departureTimeConsumer = input -> departureTimes.add(CsvParser.parseDepartureTime(input));
        Consumer<String> carrierTimeConsumer = input -> carrierTimes.add(CsvParser.parseCarrierTime(input));
        Consumer<String> itemConsumer = input -> items.add(CsvParser.parseItem(input));
        Consumer<String> orderConsumer = input -> orders.add(CsvParser.parseOrder(input));

        BufferedWriter bw = new BufferedWriter(new FileWriter("output002.txt"));
        FileInputStream fstream = new FileInputStream("input001.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String inputLine;
        Consumer<String> consumer = t -> {
        };
        while ((inputLine = br.readLine()) != null) {
            switch (inputLine) {
                case "---Orders---":
                    consumer = orderConsumer;
                    break;
                case "---Stocks---":
                    consumer = stockConsumer;
                    break;
                case "---BoxTypes---":
                    consumer = boxTypeConsumer;
                    break;
                case "---CarrierPricing---":
                    consumer = carrierPricingConsumer;
                    break;
                case "---DepartureTimes---":
                    consumer = departureTimeConsumer;
                    break;
                case "---CarrierTimes---":
                    consumer = carrierTimeConsumer;
                    break;
                case "---Items---":
                    consumer = itemConsumer;
                    break;
                default:
                    consumer.accept(inputLine);
                    break;
            }
        }
        br.close();

        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order arg0, Order arg1) {
                return arg0.getOrderDate().compareTo(arg1.getOrderDate());
            }
        });

        ShipmentsManager shipmentsManager = new ShipmentsManager(items, boxTypes, carrierPricings, departureTimes,
                carrierTimes, stocks);

        List<ShipmentInfo> shipmentInfos = orders.stream().map(shipmentsManager::findBestShipmentInfo)
                .collect(Collectors.toList());

        Collections.sort(shipmentInfos, new Comparator<ShipmentInfo>() {
            @Override
            public int compare(ShipmentInfo arg0, ShipmentInfo arg1) {
                return arg0.getOrder().getOrderDate().compareTo(arg1.getOrder().getOrderDate());
            }
        });

        Float totalShipmentPrice = 0.0f;

        for (ShipmentInfo shipmentInfo : shipmentInfos) {
            totalShipmentPrice += shipmentInfo.shippingPrice + shipmentInfo.getShippingExperiencePrice();
        }
        StringBuilder output = new StringBuilder();
        output.append(totalShipmentPrice + "\n");
        for (ShipmentInfo shipmentInfo : shipmentInfos) {
            output.append(shipmentInfo.toCsvLine() + "\n");
        }
        bw.write(output.toString());
        bw.close();
        System.out.println("Your total shipment price is: " + totalShipmentPrice);
    };

}
