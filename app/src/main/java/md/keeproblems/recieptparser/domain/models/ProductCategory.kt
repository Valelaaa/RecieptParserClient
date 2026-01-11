package md.keeproblems.recieptparser.domain.models

enum class ProductCategory {
    None,

    // Food
    Fruits,
    Vegetables,
    Meat,
    Poultry,
    Fish,
    Seafood,
    Dairy,
    Eggs,
    BreadBakery,
    CerealsGrains,
    Pasta,
    SweetsCandy,
    Snacks,
    FrozenFood,
    CannedFood,
    SaucesCondiments,
    OilsFats,
    SpicesSeasonings,
    Beverages,
    SoftDrinks,
    Water,
    Coffee,
    Tea,
    Alcohol,
    BabyFood,

    // Shopping / Household
    CleaningSupplies,
    Laundry,
    PaperProducts,
    Hygiene,
    Cosmetics,
    SkinCare,
    HairCare,
    OralCare,
    HealthCare,
    Medicine,
    VitaminsSupplements,

    // Home & Living
    KitchenSupplies,
    HomeDecor,
    Furniture,
    Bedding,
    ToolsHardware,
    Electronics,
    SmallAppliances,
    Lighting,

    // Clothing & Accessories
    Clothing,
    Shoes,
    Accessories,
    Bags,
    Sportswear,
    Underwear,

    // Tech
    Smartphones,
    Computers,
    Laptops,
    Tablets,
    AudioEquipment,
    Cameras,
    Gaming,

    // Transport
    Fuel,
    CarMaintenance,
    CarParts,
    Parking,
    PublicTransport,

    // Pets
    PetFood,
    PetSupplies,

    // Subscriptions & Services
    Streaming,
    Software,
    CloudStorage,
    MobilePlan,
    Internet,
    Utilities,

    // Misc
    Books,
    Stationery,
    Toys,
    Gifts,
    Jewelry,
    Gardening,
    DIY,
    Pharmacy,
    Tobacco
}