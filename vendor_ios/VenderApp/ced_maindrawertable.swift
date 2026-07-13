/**
 * CedCommerce
 *
 * NOTICE OF LICENSE
 *
 * This source file is subject to the End User License Agreement (EULA)
 * that is bundled with this package in the file LICENSE.txt.
 * It is also available through the world-wide-web at this URL:
 * http://cedcommerce.com/license-agreement.txt
 *
 * @category  Ced
 * @package   MageNative MultiVendor
 * @author    CedCommerce Core Team <connect@cedcommerce.com >
 * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 * @license      http://cedcommerce.com/license-agreement.txt
 */

import UIKit
//ced_maindrawertable
class ced_maindrawertable: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate,UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate {
    let screenSize = UIScreen.main.bounds
    @IBOutlet weak var drawerTable: UITableView!
    var dataMenu = [String]()
    let defaults = UserDefaults.standard
    var TransactData = [String]()
    var productData    = [String]()
    var MemberShipData = [String]()
    var Settingdata = [String]()
    var ReportData  = [String]()
    var OrderData   = [String]()
    var msgattr     = [String]()
    var auctionAttr =   [String]()
    var cmsAttr     =   [String]()
    var storeView =     [String]()
    var showMsg  =     false
    var showAuction=false
    var selectSellData = [String]()
    
    var dealsData = [String]()
    var advReportData = [String]()
    var purchaseOrderData = [String]()
    
    var Rma = [String]()
    var promotionalsArray = [String]()
    var RFQArray = [String]()
    
    var pincodechekerdata = [String]()
    var faqArray = [String]()
    var vacationArray = [String]()
    var showVacation = false
    
    
    var pincodechecker = false
    var showProduct = false
    var showTransact = false
    var showReport   = false
    var showSetting  = false
    var showOrder    = false
    
    var showSelectSell = false
    var showDeal = false;
    var showAdvReport = false;
    var showPurchase = false;
    var selectedImage:UIImage?
    
    var RmaFlag = false
    var promotional = false
    var RFQ = false
    var showCms = false
    var showStore = false
    
    
    var showMemberShip=false
    
    
    let screenwidth = UIScreen.main.bounds.width
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        print(showSetting)
        drawerTable.delegate = self
        drawerTable.dataSource = self
        drawerTable.backgroundColor = DynamicColor.themeColor
        self.sideDrawerViewController?.drawerWidth = screenwidth-100
        NotificationCenter.default.addObserver(self, selector: #selector(loadFromNotif(_:)), name: NSNotification.Name(rawValue: "loadDrawerAgain"), object: nil);
        NotificationCenter.default.post(name: NSNotification.Name("loadDrawerAgain"),object: nil);
        NotificationCenter.default.addObserver(self, selector: #selector(openGallaryNotif(_:)), name: NSNotification.Name(rawValue: "openGallaryNotif"), object: nil);
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if ced_storeVC.selectLangauge=="ar"
        {
            self.sideDrawerViewController?.drawerDirection = .right
        }
        else if ced_storeVC.selectLangauge=="en"
        {
            self.sideDrawerViewController?.drawerDirection = .left
        }
    }
        
    @objc func openGallaryNotif(_ notification: NSNotification)
    {
        self.browseImageFunc(isCamera: false)
    }
    
    @objc func loadFromNotif(_ notification: NSNotification)
    {
        //data.append("Seller Guide");
        productData.removeAll()
        dataMenu.removeAll()
        if let nav = defaults.value(forKey: "cedNaveitems") as? [String]{
            for index in nav{
                dataMenu.append(index);
            }
        }
       // dataMenu.append("Language".localized)
        vacationArray = ["Inventory Manage".localized,"Out of Stock Product".localized,"Low Stock Product".localized]
       // faqArray = ["Vendor Product Faq".localized]
        dealsData = ["Create Deals".localized,"List Deals".localized,"Deals Setting".localized];
        advReportData = ["Out Of Stock Products".localized,"Product Sales".localized,"Payment Report".localized,"Return Report".localized] //"Product Views".localized ,"Sold Products".localized
        Rma = ["Manage RMA Request".localized]
        promotionalsArray = ["Catalog Price Rules".localized,"Shopping Cart Price Rules".localized]
        RFQArray = ["Manage Quotes","Manage Proposal"]
        //MARK:- PO Section
        purchaseOrderData = ["Quotations List","Assigned Quotations","Assigned Category"]
        MemberShipData = ["Membership Plans", "Plans History"]
        storeView = ["View Stores"]
        msgattr = ["Admin Inbox".localized,"Customer Inbox".localized]
        pincodechekerdata=["Pincode List","Assigned Pincode"]
        
        if let Transact = defaults.value(forKey: "cedNavTransact") as? [String]
        {
            print("here's transaction")
            TransactData = Transact
            print(TransactData)
        }
        if let  MemberShip = defaults.value(forKey: "cedNavMemberShipPlans") as? [String]
        {
            //MemberShipData = MemberShip
        }
        if let Report = defaults.value(forKey: "cedNavReport") as? [String]
        {
            ReportData = Report
            ReportData.append(contentsOf: advReportData)
        }
        if let  Setting = defaults.value(forKey: "cedNavSett") as? [String]
        {
          //  Settingdata = Setting
        }
        if let  Order = defaults.value(forKey: "cedNavOrder") as? [String]
        {
            OrderData = Order
        }
        if let  cms = defaults.value(forKey: "cedCMSData") as? [String]
        {
            cmsAttr = cms
        }
        if let  attrs = defaults.value(forKey: "cedAttrData") as? [String]{
            productData.append(contentsOf: attrs)
            productData.append(contentsOf: vacationArray)
        }
        if(!Ced_CommonVendor().checkModule("Ced_VDealApi")){
            dealsData = [String]();
            
        }
        if(!Ced_CommonVendor().checkModule("Ced_VReportApi")){
//            advReportData = [String]();
            
        }
        if(!Ced_CommonVendor().checkModule("Ced_VPoApi")){
//            purchaseOrderData = [String]();
            
        }
        if(!Ced_CommonVendor().checkModule("Ced_VUPSShippingApi")){
            // Settingdata = ["Transaction Settings"]
            
        }
        
        if(!Ced_CommonVendor().checkModule("Ced_VOrderApi")){
            OrderData = [String]()
        }
        if(!Ced_CommonVendor().checkModule("Ced_VProductApi")){
            
        }
        if(!Ced_CommonVendor().checkModule("Ced_VAuctionApi")){
            
            auctionAttr=[String]()
        }
        if let cedSelectSell = defaults.value(forKey:"cedSelectSell") as? [String]{
            
//            selectSellData = cedSelectSell.removingDuplicates()
        }
        if(!Ced_CommonVendor().checkModule("Ced_VRmaApi")){
            Rma = [String]()
        }
        if(!Ced_CommonVendor().checkModule("Ced_VRmaApi")){
            Rma = [String]()
        }
        if(!Ced_CommonVendor().checkModule("Ced_VCmsApi")){
            cmsAttr = [String]()
        }
        if(!Ced_CommonVendor().checkModule("Ced_Vpro")){
            promotionalsArray = [String]()
        }
        
        if(!Ced_CommonVendor().checkModule("Ced_VStorepickupApi")){
            storeView = [String]()
        }
        if(!Ced_CommonVendor().checkModule("Ced_VMessagingApi")){
            msgattr = [String]()
        }
        
        if(!Ced_CommonVendor().checkModule("Ced_VRfqApi")){
            RFQArray = [String]()
        }
        
        if(!Ced_CommonVendor().checkModule("Ced_VPincodeApi")){
            pincodechekerdata = [String]()
        }
        
        
        if(!Ced_CommonVendor().checkModule("Ced_VMembershipApi")){
            MemberShipData = [String]()
        }
        
        if let index = dataMenu.index(of: "Vendor Profile"){
            dataMenu[index] = "Seller Profile".localized
        }
        drawerTable.reloadData()
        /*if let subProfileIndex = data.index(of: "Sub-Vendor Profile"){
            if let profileIndex = data.index(of: "Vendor Profile") {
                data.remove(at: subProfileIndex)
            }
        }*/
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(section == 1){
            if(showProduct){
                return productData.count + 1
            }
            return 1
        }else if(section == 2)
        {
            if(showVacation){
                return vacationArray.count + 1
            }
            if vacationArray.count == 0 {
                return 0
            }
            return 1
        }else if(section == 3){
            if(showOrder){
                return OrderData.count + 1
            }
            return 1
        }else if(section == 4){
            if(showTransact){
                return TransactData.count + 1
            }
            return 1
        }else if(section == 5)
        {
            // product review
            return 1
        }else if(section == 6){
            if(showReport){
                return ReportData.count + 1
            }
            return 1
        }else if(section == 7)
        {
            if(showAdvReport){
                return advReportData.count + 1;
            }
            if(advReportData.count == 0)
            {
                return 0;
            }
            return 1;
        }else if(section == 8){
            if(RmaFlag){
                return Rma.count + 1
            }
            if(Rma.count == 0)
            {
                return 0;
            }
            return 1
        }else if section == 9
        {
            if(showCms){
                return cmsAttr.count + 1
            }
            if(cmsAttr.count == 0)
            {
                return 0;
            }
            return 1
        }else if(section == 10)
        {
            if(showDeal){
                return dealsData.count + 1;
            }
            if(dealsData.count == 0)
            {
                return 0;
            }
            return 1;
        }else if section == 11
        {
            if(showMsg){
                return msgattr.count + 1
            }
            if(msgattr.count == 0)
            {
                return 0;
            }
            return 1
        }else if(section == 12){
            if(RFQ){
                return RFQArray.count + 1
            }
            if(RFQArray.count == 0)
            {
                return 0;
            }
            return 1
        }else if(section == 13)
        {
            if(showMemberShip){
                return MemberShipData.count + 1
            }
            if MemberShipData.count == 0 {
                return 0
            }
            return 1
        }else if section == 14{
            //vendor Product Faq
            return faqArray.count
        }else if(section == 15){
            if(showSelectSell){
                return selectSellData.count + 1
            }
            if selectSellData.count == 0 {
                return 0
            }
            return 1
        }else if(section == 16)
        {
            //PO Quotations
            if(showPurchase){
                return 0;  //purchaseOrderData.count + 1;
            }
            if(purchaseOrderData.count == 0)
            {
                return 0;
            }
            return 0;
        }else if(section == 17){
            
            if(promotional){
                return promotionalsArray.count + 1
            }
            if(promotionalsArray.count == 0)
            {
                return 0;
            }
            return 1
        }else if(section == 18)
        {
            if(showStore){
                return storeView.count + 1
            }
            if(storeView.count == 0)
            {
                return 0;
            }
            return 1
        }else if(section == 19)
        {
            if(showAuction){
                //return auctionAttr.count + 1
            }
            if auctionAttr.count==0{
                return 0
            }
            return 0
        }else if(section == 20)
        {
            if(pincodechecker){
                return pincodechekerdata.count + 1
            }
            if pincodechekerdata.count == 0 {
                return 0
            }
            return 1
        }else if(section == 21){
            return 1
        }else if(section == 22){
            print(showSetting)
            if(showSetting){
                return Settingdata.count + 1
            }
            return 1
        }
        //for section 0
        return dataMenu.count + 1
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 23
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(indexPath.section == 1){
            if(showProduct){
                if(indexPath.row > 0){
                    if let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell{
                        cell.label.font=UIFont(name: "Roboto-Medium", size: 12)
                        cell.label.text = "-"+productData[indexPath.row - 1]
                        cell.label.font = cell.label.font.withSize(12)
                        cell.imageV.image = nil
                        return cell
                    }
                    return UITableViewCell()
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Products".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "managepro")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.text = "Products".localized
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "managepro")
                return cell!
            }
            
            
        }else if(indexPath.section == 2){
            print(indexPath.section)
            if(showVacation){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+self.vacationArray[indexPath.row - 1]
                    print(self.vacationArray[indexPath.row - 1])
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Vendor Inventory".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "select")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.text = "Vendor Inventory".localized
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "select")
                return cell!
            }
        }else  if(indexPath.section == 3){
            if(showOrder){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+OrderData[indexPath.row - 1]
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Orders".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.imageV.image = UIImage(named: "shopcart")
                    cell?.label.font = cell?.label.font.withSize(15)
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.text = "Orders".localized
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "shopcart")
                return cell!
            }
            
        }else if(indexPath.section == 4){
            if(showTransact){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.text = "-"+TransactData[indexPath.row - 1]
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Transaction".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "dollor")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.text = "Transaction".localized
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "dollor")
                return cell!
            }
            
        }else if(indexPath.section == 5){
            print(indexPath.section)
            
            let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
            cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
            cell?.label.text = "Product Review".localized
            cell?.label.font = cell?.label.font.withSize(15)
            cell?.imageV.image = UIImage(named: "StarFilled")?.maskWithColor(color: .white)
            return cell!
        }else  if(indexPath.section == 6){
            if(showReport){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+ReportData[indexPath.row - 1]
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.text = "Reports".localized
                    cell?.imageV.image = UIImage(named: "barchartcart")
                    cell?.label.font = cell?.label.font.withSize(15)
                    
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.text = "Reports".localized
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "barchartcart")
                return cell!
            }
            
        }else if(indexPath.section == 7)
        {
            if(showAdvReport){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+advReportData[indexPath.row - 1]
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Advance Report".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "select")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.text = "Advance Report".localized
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "select")
                return cell!
            }
        }else if(indexPath.section == 8){
            if(RmaFlag){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+Rma[indexPath.row - 1]
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "RMA".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "exchange")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.text = "RMA".localized
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "exchange")
                return cell!
            }
        }else  if(indexPath.section == 9){
            if(showCms){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+cmsAttr[indexPath.row - 1]
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Seller CMS".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "documents")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.text = "Seller CMS".localized
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "documents")
                return cell!
            }
            
        }else  if(indexPath.section == 10){
            if(showDeal){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+dealsData[indexPath.row - 1]
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Seller Deals".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "gift")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.text = "Seller Deals".localized
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "gift")
                return cell!
            }
            
        }else  if(indexPath.section == 11){
            if(showMsg){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+msgattr[indexPath.row - 1]
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Messaging".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "money")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.text = "Messaging".localized
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "money")
                return cell!
            }
        }else  if(indexPath.section == 12){
            if(RFQ){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+RFQArray[indexPath.row - 1]
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Request For Quotation".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "membership")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.text = "Request For Quotation".localized
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "membership")
                return cell!
            }
            
        }else if(indexPath.section == 13){
            if(showMemberShip){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = MemberShipData[indexPath.row - 1].localized
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Membership Plans".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "membership")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.text = "Membership Plans".localized
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "membership")
                return cell!
            }
            
        }else if(indexPath.section == 14){
            print(indexPath.section)
            
            let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
            cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
            cell?.label.text = "Vendor Product Faq".localized
            cell?.label.font = cell?.label.font.withSize(15)
            cell?.imageV.image = UIImage(named: "membership")
            return cell!
        }else if (indexPath.section == 15) {
            if(showSelectSell){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+selectSellData[indexPath.row - 1]
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                    
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Select and Sell".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "select")
                    return cell!
                    
                }
            }else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.text = "Select and Sell".localized
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "select")
                return cell!
                
            }
        }else if(indexPath.section == 16)
        {
            if(showPurchase){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+purchaseOrderData[indexPath.row - 1]
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "PO Quotations".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "message")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.text = "PO Quotations".localized
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "message")
                return cell!
            }
        }else  if(indexPath.section == 17){
            if(promotional){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+promotionalsArray[indexPath.row - 1]
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Promotions".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "settings")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.text = "Promotions".localized
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "settings")
                return cell!
            }
            
        }else if(indexPath.section == 18){
            if(showStore){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+storeView[indexPath.row - 1]
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Store Pickup".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "users")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.text = "Store Pickup".localized
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "users")
                return cell!
            }
            
        }else  if(indexPath.section == 19){
            if(showAuction){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+auctionAttr[indexPath.row - 1]
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Auction".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "gavel")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.text = "Auction".localized
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "gavel")
                return cell!
            }
            //            drawer    MKEmbedDrawerControllerSegue
        }else if(indexPath.section == 20){
            print(indexPath.section)
            if(pincodechecker){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+self.pincodechekerdata[indexPath.row - 1]
                    print(self.pincodechekerdata[indexPath.row - 1])
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Pincode Checker".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "membership")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.text = "Pincode Checker".localized
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "membership")
                return cell!
            }
        }else  if(indexPath.section == 21){
            let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
            cell?.label.text = "Language".localized
            cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
            cell?.label.font = cell?.label.font.withSize(15)
            cell?.imageV.image = UIImage(named: "settings")
            return cell!
        }else  if(indexPath.section == 22){
            if(showSetting){
                if(indexPath.row > 0){
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "-"+Settingdata[indexPath.row - 1]
                    cell?.label.font=UIFont(name: "Roboto-Medium", size: 12)
                    cell?.label.font = cell?.label.font.withSize(12)
                    cell?.imageV.image = nil
                    return cell!
                }else{
                    let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                    cell?.label.text = "Settings".localized
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.label.font = cell?.label.font.withSize(15)
                    cell?.imageV.image = UIImage(named: "settings")
                    return cell!
                }
            }
            else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                cell?.label.text = "Settings".localized
                cell?.label.font = cell?.label.font.withSize(15)
                cell?.imageV.image = UIImage(named: "settings")
                return cell!
            }
            
        }else{
            if(indexPath.row == 0){
                let cell = tableView.dequeueReusableCell(withIdentifier: "profileCell") as? ced_mainDrawerprofileCell
                let userData = defaults.object(forKey: "userInfoDict") as? NSDictionary
                let vendorName = userData?["vendorName"] as? String ?? ""
                cell?.label1.text = "Hello,".localized + " \(vendorName)"
                cell?.label1.font=UIFont(name: "Roboto-Bold", size: 17)
                cell?.imageViee.layer.cornerRadius =  cell!.imageViee.frame.size.width/2
                cell?.imageViee.clipsToBounds = true
                cell?.imageViee.layer.masksToBounds = true
                cell?.imageViee.layer.borderWidth = 3
                cell?.imageViee.layer.borderColor = UIColor.white.cgColor
                cell?.imageViee.isUserInteractionEnabled = true;
                let tapRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.imageTapped(_:)))
                cell?.imageViee.addGestureRecognizer(tapRecognizer);
                if(self.selectedImage != nil){
                    cell?.imageViee.image = self.selectedImage
                    cell?.imageViee.contentMode = .scaleAspectFill
                }else if let url = UserDefaults.standard.value(forKey: "ProfilePic") as? String
                {
                    let urltoreq = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
                    cell?.imageViee.sd_setImage(with: URL(string: urltoreq ?? ""), placeholderImage: UIImage(named: "profle"))
                    cell?.imageViee.contentMode = .scaleAspectFill
                }
                return cell!
            }else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "normalCell") as? ced_maindrawernormalcell
                cell?.label.text = dataMenu[indexPath.row-1]
                print(dataMenu[indexPath.row-1])
                if(dataMenu[indexPath.row-1] == "Dashboard Menu".localized){
                    cell?.imageV.image = UIImage(named: "dashboard")?.maskWithColor(color: .white)
                }else if(dataMenu[indexPath.row-1] == "Products".localized){
                    cell?.imageV.image = UIImage(named: "managepro")
                }else if(dataMenu[indexPath.row-1] == "Seller Guide".localized){
                    cell?.imageV.image = UIImage(named: "guide_icon")
                    cell?.imageV.tintColor = UIColor.white;
                }else if(dataMenu[indexPath.row-1] == "Seller Profile".localized){
                    cell?.imageV.image = UIImage(named: "vendorprofile")
                }else if(dataMenu[indexPath.row-1] == "New Product".localized){
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.imageV.image = UIImage(named: "newpro")
                }else if(dataMenu[indexPath.row-1] == "Table Rate Shipping".localized){
                    cell?.label.font=UIFont(name: "Roboto-Bold", size: 15)
                    cell?.imageV.image = UIImage(named: "tablerate_shipping_icon")
                    cell?.imageV.tintColor = UIColor.white;
                }else if(dataMenu[indexPath.row-1] == "Manage Products".localized){
                    cell?.imageV.image = UIImage(named: "managepro")
                }else if(dataMenu[indexPath.row-1] == "Orders".localized){
                    cell?.imageV.image = UIImage(named: "shopcart")
                }else if(dataMenu[indexPath.row-1] == "Transactions".localized){
                    cell?.imageV.image = UIImage(named: "dashboard")
                }else if(dataMenu[indexPath.row-1] == "Reports".localized){
                    cell?.imageV.image = UIImage(named: "dashboard")
                }else if(dataMenu[indexPath.row-1] == "Settings".localized){
                    cell?.imageV.image = UIImage(named: "dashboard")
                }
                else if(dataMenu[indexPath.row-1] == "Sub-Vendor Profile".localized){
                    cell?.imageV.image = UIImage(named: "dashboard")
                }
                return cell!
            }
        }
        return UITableViewCell()
    }
    
    
    @objc func imageTapped(_ gestureRecognizer: UITapGestureRecognizer)
    {
        /* background view */
        let bgCView = UIView();
        bgCView.tag=121;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        
        self.addgesturesTohideView(self.view);
        
        let imgPopupView = UIImageView();
        imgPopupView.tag = 131;
        imgPopupView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        imgPopupView.frame = CGRect(x: 0, y: CGFloat(0), width: screenSize.width/2,height: screenSize.height/2);
        imgPopupView.center = CGPoint(x: bgCView.frame.size.width  / 2,
                                      y: bgCView.frame.size.height / 2);
        let tappedImageView = gestureRecognizer.view as! UIImageView;
        imgPopupView.image = tappedImageView.image;
        
        bgCView.addSubview(imgPopupView);
       // self.view.addSubview(bgCView);
        self.browseImageFunc(isCamera: false)
       
    }
    
    let imagePicker = UIImagePickerController()
    @objc func browseImageFunc(isCamera:Bool)
    {
        print("inside browseFunc")
        imagePicker.delegate = self
        if(isCamera){
            imagePicker.sourceType = .camera
        }else {
            imagePicker.sourceType = .photoLibrary
            imagePicker.mediaTypes = ["public.image"]
        }
        imagePicker.allowsEditing = false
        self.present(imagePicker, animated: true, completion: nil)
    }
    
    @objc func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        
        if let image = info[UIImagePickerController.InfoKey.editedImage] as? UIImage {
            self.selectedImage = image
        } else if let image = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
            self.selectedImage = image
        }
        self.drawerTable.reloadRows(at: [IndexPath(row: 0, section: 0)], with: .none)
        var postString = "";
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        postString += "vendor_id="+vendor_id+"&hashkey="+hashkey;
        if(self.selectedImage != nil){
            let tempData=self.selectedImage!.jpegData(compressionQuality: 0.5)!
            let chat_file=(tempData.base64EncodedString())
            let dictionary = ["name":"124.jpg","base64_encoded_data":chat_file]
            let convertedstring=convertDicTostring(str: dictionary)
            postString += "&profile_picture="+convertedstring as String
            self.sendRequest(url: "vendorapi/index/update", parameters: postString)
        }
        imagePicker.dismiss(animated: true, completion: nil);
    }
    
    @objc func sendRequest(url: String,parameters: String){
        
        var baseUrl = settings.baseUrl
        baseUrl += url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!
        var request =  URLRequest(url: URL(string:baseUrl)!)
        
        request.httpMethod = "POST"
        request.httpBody = parameters.data(using: String.Encoding.utf8)
       // print(parameters)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        print(request)
        print(parameters)
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request, completionHandler: {
            data,response,error in
            DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
            }
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        
                        Alert_File.removeLoadingIndicator(self)
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                }
                print("error=\(error)")
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                DispatchQueue.main.async
                    {
                        
                        Alert_File.removeLoadingIndicator(self)
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        print("response = \(response)")
                        print(baseUrl)
                        //self.verdorOrderTable.isHidden = true
                        if(httpStatus.statusCode == -1009){
                            ced_showError.showNoNetWork(self)
                        }else{
                            ced_showError.showNoModule(self)
                        }
                }
                return;
            }
            DispatchQueue.main.async
                {
                    print(NSString(data: data!, encoding: String.Encoding.utf8.rawValue)!)
                    //let json = JSON(data: data!)
                    Alert_File.removeLoadingIndicator(self)
            }
            
            
        })
        task.resume()
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 0){
            if(indexPath.row == 0){
                return 205
            }else{
                return 50
            }
        }else if(indexPath.section == 2){
            return 0
        }else if(indexPath.section == 5){
            return 0
        }else if(indexPath.section == 7){
            return 0
        }else if(indexPath.section == 8){
            return 0
        }else if(indexPath.section == 22){
            return 0
        }
        return 50
        
    }
    
    
    
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    //@objc function to dismiss the custom popover
    @objc func hideView(_ recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(121)?.removeFromSuperview()
        
        
    }
    
    
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        
        if( touch.view!.isDescendant(of: drawerTable))
        {
            return false;
        }
        return true;
        
    }
    
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("ROW&&&***&*&*&*&*&*&*&*\(indexPath.row)")
        print("SECTION*&*&*&*&*&*&*&*&*&\(indexPath.section)")
        let navigation = self.sideDrawerViewController?.mainViewController as! ced_navigationBarController
        if indexPath.section == 0{
            if(indexPath.row == 1){
                print("^^^&^&^&^&^&YYYYYHHHJKVJVJVHJHJ***&&(()()()()(")
                self.sideDrawerViewController?.toggleDrawer()
                let viewControl = storyboard?.instantiateViewController(withIdentifier: "dashRootView") as! UINavigationController
                self.sideDrawerViewController?.transitionFromMainViewController(viewControl, duration: 1, options: UIView.AnimationOptions.transitionFlipFromTop, animations: nil, completion: nil)
                return
            }else if(indexPath.row == 2){
                print("^^^&^&^&^&^&YYYYYHHHJKVJVJVHJHJ***&&(()()()()(")
                let storybordOrder = UIStoryboard(name: "Main", bundle: nil)
                self.sideDrawerViewController?.toggleDrawer()
                let viewControl = storybordOrder.instantiateViewController(withIdentifier: "vendorProfileViewController") as! VendorProfileViewController
                navigation.pushViewController(viewControl, animated: true)
                return
            }
        }else if(indexPath.section == 1){
            if(indexPath.row == 0){
                if(showProduct){
                    showProduct = false
                    let section = IndexSet(integer: 1)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 1)
                    showProduct = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: productData.count, section: 1), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                let navemenu = productData[indexPath.row-1]
                if(navemenu == "Manage Attribute".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let storybordOrder = UIStoryboard(name: "ProductAttributes", bundle: nil)
                    let viewControl = storybordOrder.instantiateViewController(withIdentifier: "vendor_attributeListing") as! ced_vendorProductAttributesListing
                    navigation.pushViewController(viewControl, animated: true)
                    
                    return
                }
                else if(navemenu == "Manage Attribute Set".localized){                    self.sideDrawerViewController?.toggleDrawer()
                    let storybordOrder = UIStoryboard(name: "ProductAttributes", bundle: nil)
                    let viewControl = storybordOrder.instantiateViewController(withIdentifier: "cedVendorAttrSet") as! ced_vendorAttributeSet
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }else if(navemenu == "Manage Products".localized){
                    let storybordOrder = UIStoryboard(name: "ProductAddon", bundle: nil)
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = storybordOrder.instantiateViewController(withIdentifier: "manageProductViewController") as! ManageProductViewController
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }else if(navemenu == "New Product".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let storyBoard = UIStoryboard(name: "ProductAddon", bundle: nil)
                    let viewControl = storyBoard.instantiateViewController(withIdentifier: "addProductAddonFirstPage") as! AddProductAddonFirstPage
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }else if(navemenu == "Inventory Manage".localized){
                    print("test1")
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = ced_manageInventoryViewController()
                    navigation.pushViewController(viewControl, animated: true)

                    return
                }
                else if(navemenu == "Out of Stock Product".localized){
                    print("test2")
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = ced_outOfStockInventoryViewController()
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Low Stock Product".localized){
                    print("test3")
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = ced_lowStockInventoryViewController()
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
            
            
        }else if(indexPath.section == 2){
            if(indexPath.row == 0){
                let section = IndexSet(integer: 2)
                if(showVacation){
                    showVacation = false
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    showVacation = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: self.vacationArray.count, section: 2), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                let navemenu = self.vacationArray[indexPath.row-1]
                if(navemenu == "Inventory Manage".localized){
                    print("test1")
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = ced_manageInventoryViewController()
                    navigation.pushViewController(viewControl, animated: true)

                    return
                }
                else if(navemenu == "Out of Stock Product".localized){
                    print("test2")
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = ced_outOfStockInventoryViewController()
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Low Stock Product".localized){
                    print("test3")
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = ced_lowStockInventoryViewController()
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
        }else if(indexPath.section == 3){
            let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
            if(indexPath.row == 0){
                print("1")
                if(OrderData.count == 0){
                    print("2")
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = storyboard?.instantiateViewController(withIdentifier: "vendorOrders") as! ced_vendorOrderpannel
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                if(showOrder){
                    print("3")
                    showOrder = false
                    let section = IndexSet(integer: 3)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    print("4")
                    let section = IndexSet(integer: 3)
                    showOrder = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: OrderData.count, section: 3), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                let navemenu = OrderData[indexPath.row-1]
                if(navemenu == "Manage Orders".localized){
                    print("5")
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = storyboard?.instantiateViewController(withIdentifier: "vendorOrders") as! ced_vendorOrderpannel
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }else if(navemenu == "Cancel Orders".localized){
                    print("5")
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = storyboard?.instantiateViewController(withIdentifier: "vendorOrders") as! ced_vendorOrderpannel
                    viewControl.applyFilter = true
                    viewControl.filterString = "{\"created_at\":{\"from\":\"\",\"to\":\"\"},\"payment_state\":\"\",\"shop_commission_fee\":{\"to\":\"\",\"from\":\"\"},\"increment_id\":\"\",\"order_payment_state\":\"3\",\"order_total\":{\"to\":\"\",\"from\":\"\"},\"net_vendor_earn\":{\"from\":\"\",\"to\":\"\"},\"billing_name\":\"\"}"
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Manage Invoice".localized){
                    print("6")
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = storybordOrder.instantiateViewController(withIdentifier: "manageInvoice") as! ced_InvoiceList
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Manage Shipment".localized){
                    print("7")
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = storybordOrder.instantiateViewController(withIdentifier: "manageshipment") as! ced_shipmentListing
                    navigation.pushViewController(viewControl, animated: true)
                    return
                } else if(navemenu == "Manage Credit Memo".localized){
                    print("8")
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = storybordOrder.instantiateViewController(withIdentifier: "manageCreditmemo") as! ced_creditmemoListing
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }else if(navemenu == "Manage RMA Request".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ced_ViewControllerRmaView") as! ced_ViewControllerRmaView
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
            
        }else if(indexPath.section == 4){
            if(indexPath.row == 0){
                if(showTransact){
                    showTransact = false
                    let section = IndexSet(integer: 4)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 4)
                    showTransact = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: TransactData.count, section: 4), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                print("transacionArray")
                print(TransactData)
                let navemenu = TransactData[indexPath.row-1]
                if(navemenu == "Requested Transaction".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = storyboard?.instantiateViewController(withIdentifier: "cedRequestedTransactions") as!cedRequestedTransactions
                    navigation.pushViewController(viewControl, animated: true)
                    
                    return
                }
                else if(navemenu == "View Transactions".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = storyboard?.instantiateViewController(withIdentifier: "viewtransaction") as! ced_viewTransactions
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }else if(navemenu == "Transaction Settings".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = storyboard?.instantiateViewController(withIdentifier: "vendorTransactionSettings") as! VendorTransactionSettings
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
            
            
        }else if(indexPath.section == 5){
            if(indexPath.row == 0){
                self.sideDrawerViewController?.toggleDrawer()
                let viewControl = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "managereviews") as! ced_ManageReviews
                navigation.pushViewController(viewControl, animated: true)
                return
            }
        }else if(indexPath.section == 6){
            if(indexPath.row == 0){
                if(showReport){
                    showReport = false
                    let section = IndexSet(integer: 6)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 6)
                    showReport = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: ReportData.count, section: 6), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }
            else{
                let navemenu = ReportData[indexPath.row-1]
                if(navemenu == "Order Reports".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = storyboard?.instantiateViewController(withIdentifier: "reportTimeSelectorViewController") as! ReportTimeSelectorViewController
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Product Reports".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = storyboard?.instantiateViewController(withIdentifier: "productReportRangeViewController") as! ProductReportRangeViewController
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }else if(navemenu == "Out Of Stock Products".localized){
                    
                    self.sideDrawerViewController?.toggleDrawer()
                    
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedOutOfStockListing") as! cedOutOfStockListing
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Sold Products".localized){                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedSoldProducts") as! cedSoldProducts
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Product Views".localized){                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedProductViews") as! cedProductViews
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Product Sales".localized){                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedSalesReport") as! cedSalesReport
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Payment Report".localized){                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedPaymentListing") as! cedPaymentListing
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Return Report".localized){                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedProductReturn") as! cedProductReturn
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
        }else if(indexPath.section == 7)
        {
            if(indexPath.row == 0){
                if(showAdvReport){
                    showAdvReport = false
                    let section = IndexSet(integer: 7)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 7)
                    showAdvReport = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: advReportData.count, section: 7), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                
                let navemenu = advReportData[indexPath.row-1]
                if(navemenu == "Out Of Stock Products".localized){
                    
                    self.sideDrawerViewController?.toggleDrawer()
                    
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedOutOfStockListing") as! cedOutOfStockListing
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Sold Products".localized){                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedSoldProducts") as! cedSoldProducts
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Product Views".localized){                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedProductViews") as! cedProductViews
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Product Sales".localized){                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedSalesReport") as! cedSalesReport
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Payment Report".localized){                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedPaymentListing") as! cedPaymentListing
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Return Report".localized){                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedProductReturn") as! cedProductReturn
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
        }else if(indexPath.section == 8)
        {
            if(indexPath.row == 0){
                if(RmaFlag){
                    RmaFlag = false
                    let section = IndexSet(integer: 8)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 8)
                    
                    RmaFlag = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: Rma.count, section: 8), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                let stoy = UIStoryboard(name: "Main", bundle: nil)
                let navemenu = Rma[indexPath.row-1]
                print("###")
                print(navemenu)
                if(navemenu == "Manage RMA Request".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = stoy.instantiateViewController(withIdentifier: "ced_ViewControllerRmaView") as! ced_ViewControllerRmaView
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
        }else if(indexPath.section == 9){
            if(indexPath.row == 0){
                if(showCms){
                    showCms = false
                    let section = IndexSet(integer: 9)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 9)
                    showCms = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: cmsAttr.count, section: 9), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                let stoy = UIStoryboard(name: "Main", bundle: nil)
                let navemenu = cmsAttr[indexPath.row-1]
                print("###")
                print(navemenu)
                if(navemenu == "Manage Seller CMS".localized){
                    
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = stoy.instantiateViewController(withIdentifier: "NewBlockCmsViewController") as! ced_NewBlockCmsViewController
                    viewControl.isLoadedFrom = true
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu ==  "Manage Static Blocks".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = stoy.instantiateViewController(withIdentifier: "NewBlockCmsViewController") as! ced_NewBlockCmsViewController
                    viewControl.isLoadedFrom = false
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
        }else if(indexPath.section == 10)
        {
            if(indexPath.row == 0){
                if(showDeal){
                    showDeal = false
                    let section = IndexSet(integer: 10)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 10)
                    showDeal = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: dealsData.count, section: 10), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                let stoy = UIStoryboard(name: "Main", bundle: nil)
                let navemenu = dealsData[indexPath.row-1]
                if(navemenu == "Deals Setting".localized){
                    
                    self.sideDrawerViewController?.toggleDrawer()
                    
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedDealSetting") as! cedDealSetting
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Create Deals".localized){                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedDealProducts") as! cedDealProducts
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "List Deals".localized){                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedDealListing") as! cedDealListing
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
        }else if(indexPath.section == 11)
        {
            if(indexPath.row == 0)
            {
                if(showMsg){
                    showMsg = false
                    let section = IndexSet(integer: 11)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 11)
                    showMsg = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: msgattr.count, section: 11), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                let stoy = UIStoryboard(name: "Main", bundle: nil)
                let navemenu = msgattr[indexPath.row-1]
                print("###")
                print(navemenu)
                if(navemenu == "Admin Inbox".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = stoy.instantiateViewController(withIdentifier: "ced_VendorAdminController") as! ced_VendorAdminController
                 //   viewControl.isCustomer = false
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Customer Inbox".localized){
                    
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = stoy.instantiateViewController(withIdentifier: "ced_VendorCustomerController") as! ced_VendorCustomerController
                  //  viewControl.isCustomer = true
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                
            }
        }else  if(indexPath.section == 12){
            if(indexPath.row == 0){
                if(RFQ){
                    RFQ = false
                    let section = IndexSet(integer: 12)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 12)
                    RFQ = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: RFQArray.count, section: 12), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                let stoy = UIStoryboard(name: "Main", bundle: nil)
                let navemenu = RFQArray[indexPath.row-1]
                print("###")
                print(navemenu)
                if(navemenu == "Manage Quotes".localized){
                    
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = stoy.instantiateViewController(withIdentifier: "ced_ManageQuotes") as! ced_ManageQuotes
                    navigation.pushViewController(viewControl, animated: true)
                    return
//                }else if(navemenu == "View Po"){
//
                }
                else
                {
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = stoy.instantiateViewController(withIdentifier: "ced_ViewPo") as! ced_ViewPo
                    
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
        }else if(indexPath.section == 13){
            if(indexPath.row == 0){
                if(showMemberShip){
                    showMemberShip = false
                    let section = IndexSet(integer: 13)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 13)
                    showMemberShip = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: MemberShipData.count, section: 13), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                let navemenu = MemberShipData[indexPath.row-1]
                if(navemenu == "Membership Plans".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                   // let viewControl = UIStoryboard(name: "memberShipAddon", bundle: nil).instantiateViewController(withIdentifier: "memberShip") as! ced_MemberShipPlansController
                    navigation.pushViewController(ced_membershipPlanNewViewController(), animated: true)
                    
                    return
                }
                else if(navemenu == "Plans History".localized){
                    self.sideDrawerViewController?.toggleDrawer()
//                    let viewControl = UIStoryboard(name: "memberShipAddon", bundle: nil).instantiateViewController(withIdentifier: "planHistory") as! ced_PlanHistoryViewController
                    navigation.pushViewController(ced_planHistoryNewViewController(), animated: true)
                    return
                }
            }
        }else if(indexPath.section == 14){
            if(indexPath.row == 0){
                self.sideDrawerViewController?.toggleDrawer()
                let viewControl = ced_faqViewController()
                navigation.pushViewController(viewControl, animated: true)
                return
            }
        }else if(indexPath.section == 15){
            if(indexPath.row == 0){
                if(showSelectSell){
                    showSelectSell = false
                    let section = IndexSet(integer: 15)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 15)
                    showSelectSell = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: selectSellData.count, section: 15), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                let stoy = UIStoryboard(name: "selectAndSellAddon", bundle: nil)
                let navemenu = selectSellData[indexPath.row-1]
                if(navemenu == "Add Product".localized){
                    
                    self.sideDrawerViewController?.toggleDrawer()
//                    let viewControl = stoy.instantiateViewController(withIdentifier: "cedSelectSellProduct") as! cedVendorSelectAddProduct
//                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Product List".localized){
                    self.sideDrawerViewController?.toggleDrawer()
//                    let viewControl = stoy.instantiateViewController(withIdentifier: "cedSelecProductList") as! cedVendorSelectProductList
//                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                
            }
            return
        }else if(indexPath.section == 16)
        {
            if(indexPath.row == 0){
                if(showPurchase){
                    showPurchase = false
                    let section = IndexSet(integer: 16)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 16)
                    showPurchase = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: purchaseOrderData.count, section: 16), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                
                let navemenu = purchaseOrderData[indexPath.row-1]
                if(navemenu == "Quotations List".localized){
                    
                    self.sideDrawerViewController?.toggleDrawer()
                    
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedQuotationsListing") as! cedQuotationsListing
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Assigned Quotations".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "ced_deals", bundle: nil).instantiateViewController(withIdentifier: "cedAssignedQuotations") as! cedAssignedQuotations
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }else if(navemenu == "Assigned Category".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ced_assignedCategoryController") as! ced_assignedCategoryController
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
        }else if(indexPath.section == 17){
            if(indexPath.row == 0){
                if(promotional){
                    promotional = false
                    let section = IndexSet(integer: 17)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 17)
                    promotional = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: promotionalsArray.count, section: 17), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                let stoy = UIStoryboard(name: "Main", bundle: nil)
                let navemenu = promotionalsArray[indexPath.row-1]
                print("###")
                print(navemenu)
                if(navemenu == "Catalog Price Rules".localized){
                    
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = stoy.instantiateViewController(withIdentifier: "CatalogPriceRules") as! CatalogPriceRules
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Shopping Cart Price Rules".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = stoy.instantiateViewController(withIdentifier: "CatalogPriceRules") as! CatalogPriceRules
                    viewControl.ShoppingCartPriceRules=true
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
        }else if(indexPath.section == 18){
            if(indexPath.row == 0){
                if(showStore){
                    showStore = false
                    let section = IndexSet(integer: 18)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 18)
                    showStore = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: storeView.count, section: 18), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                let stoy = UIStoryboard(name: "Main", bundle: nil)
                let navemenu = storeView[indexPath.row-1]
                print(navemenu)
                if(navemenu == "View Stores".localized){
                    self.sideDrawerViewController?.toggleDrawer()
//                    let viewControl = stoy.instantiateViewController(withIdentifier: "ViewStore") as! ced_StoreViewController
//                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
        }else if(indexPath.section == 19)
        {
            if(indexPath.row == 0)
            {
                if(showAuction){
                    showAuction = false
                    let section = IndexSet(integer: 19)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 19)
                    showAuction = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: msgattr.count, section: 19), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                
                let navemenu = auctionAttr[indexPath.row-1]
                if(navemenu == "Add Auction".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    
                    let storybordOrder = UIStoryboard(name: "ProductAddon", bundle: nil)
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = storybordOrder.instantiateViewController(withIdentifier: "manageProductViewController") as! ManageProductViewController
                    viewControl.isAuction=true
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "List Auction".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let storybordOrder = UIStoryboard(name: "auctionStoryboard", bundle: nil)
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = storybordOrder.instantiateViewController(withIdentifier: "listAuctionViewController") as! listAuctionViewController
                    
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
        }else if(indexPath.section == 20){
            if(indexPath.row == 0){
                if(pincodechecker){
                    pincodechecker = false
                    let section = IndexSet(integer: 20)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 20)
                    pincodechecker = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: self.pincodechekerdata.count, section: 20), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                let navemenu = self.pincodechekerdata[indexPath.row-1]
                if(navemenu == "Pincode List".localized){
                    print("test1")
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ced_PincodeCheckerController") as! ced_PincodeCheckerController
                    viewControl.check = true
                    navigation.pushViewController(viewControl, animated: true)

                    return
                }
                else if(navemenu == "Assigned Pincode".localized){
                    print("test1")
                    self.sideDrawerViewController?.toggleDrawer()

                    let viewControler = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ced_PincodeCheckerController") as! ced_PincodeCheckerController
                    viewControler.check=false
                    navigation.pushViewController(viewControler, animated: true)
                    return
                }
            }
        }else if(indexPath.section == 21) {
            self.sideDrawerViewController?.toggleDrawer()
                      let story = UIStoryboard(name: "Main", bundle: nil)
                      let viewControl = story.instantiateViewController(withIdentifier: "ced_storeVC") as! ced_storeVC
                      viewControl.isFromDrawer = true
                      navigation.pushViewController(viewControl, animated: true)
        }else if(indexPath.section == 22){
            if(indexPath.row == 0){
                if(showSetting){
                    showSetting = false
                    let section = IndexSet(integer: 22)
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                }else{
                    let section = IndexSet(integer: 22)
                    showSetting = true
                    self.drawerTable.reloadSections(section, with: UITableView.RowAnimation.automatic)
                    self.drawerTable.scrollToRow(at: IndexPath(row: Settingdata.count, section: 22), at: UITableView.ScrollPosition.bottom, animated: true)
                }
                return
            }else{
                let stoy = UIStoryboard(name: "Main", bundle: nil)
                let navemenu = Settingdata[indexPath.row-1]
                if(navemenu == "Shipping Settings".localized){
                    
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = stoy.instantiateViewController(withIdentifier: "shippingSettingController") as! ShippingSettingController
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
                else if(navemenu == "Shipping Methods".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = stoy.instantiateViewController(withIdentifier: "ced_NewShippingMethodVC") as! ced_NewShippingMethodVC
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
               /* else if(navemenu == "Shipping Methods"){                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = stoy.instantiateViewController(withIdentifier: "shippingMethodsController") as! ShippingMethodsController
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }*/
               /* else if(navemenu == "Shipping Methods"){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = stoy.instantiateViewController(withIdentifier: "shippingMethod") as! ced_ShippingMethodViewController
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }*/
                 else if(navemenu == "Table Rate Shipping".localized){
                    self.sideDrawerViewController?.toggleDrawer()
                    let viewControl = stoy.instantiateViewController(withIdentifier: "vendorTableRateSettings") as! cedVendorTableRateShipping
                    navigation.pushViewController(viewControl, animated: true)
                    return
                }
            }
        }
       /*
        if(indexPath.row == 0){
            self.sideDrawerViewController?.toggleDrawer()
            let viewControl = storyboard?.instantiateViewController(withIdentifier: "dashRootView") as! ced_navigationBarController
            self.sideDrawerViewController?.transitionFromMainViewController(viewControl, duration: 1, options: UIView.AnimationOptions.transitionFlipFromTop, animations: nil, completion: nil)
            return
        }
        
        let navData = dataMenu[indexPath.row-1]
        if(navData == "Product Attributes"){
            let storybordOrder = UIStoryboard(name: "ProductAttributes", bundle: nil)
            self.sideDrawerViewController?.toggleDrawer()
            let viewControl = storybordOrder.instantiateViewController(withIdentifier: "vendor_attributeListing") as! ced_vendorProductAttributesListing
            navigation.pushViewController(viewControl, animated: true)
        }
        if(navData == "Dashboard"){
            self.sideDrawerViewController?.toggleDrawer()
            let viewControl = storyboard?.instantiateViewController(withIdentifier: "dashRootView") as! UINavigationController
            self.sideDrawerViewController?.transitionFromMainViewController(viewControl, duration: 1, options: UIView.AnimationOptions.transitionFlipFromTop, animations: nil, completion: nil)
        }else if(navData == "Table Rate Shipping"){
            let stoy = UIStoryboard(name: "Main", bundle: nil)
            self.sideDrawerViewController?.toggleDrawer()
            let viewControl = stoy.instantiateViewController(withIdentifier: "vendorTableRateSettings") as! cedVendorTableRateShipping
            navigation.pushViewController(viewControl, animated: true)
            return
        }else if(navData == "Orders"){
            self.sideDrawerViewController?.toggleDrawer()
            let viewControl = storyboard?.instantiateViewController(withIdentifier: "vendorOrders") as! ced_vendorOrderpannel
            navigation.pushViewController(viewControl, animated: true)
            
        }else if(navData == "Request Payments"){
            let viewControl = storyboard?.instantiateViewController(withIdentifier: "") as! ced_vendordashboard
            self.navigationController?.pushViewController(viewControl, animated: true)
            
        }else if(navData == "Order Reports"){
            let viewControl = storyboard?.instantiateViewController(withIdentifier: "") as! ced_vendordashboard
            self.navigationController?.pushViewController(viewControl, animated: true)
        }
        else if(navData == "Product Reports"){
            let viewControl = storyboard?.instantiateViewController(withIdentifier: "") as! ced_vendordashboard
            self.navigationController?.pushViewController(viewControl, animated: true)
        }
        else if(navData == "Sub-Vendor Profile"){
            print("UUUUUUUUUUUUUU")
            self.sideDrawerViewController?.toggleDrawer()
            let vc=UIStoryboard(name: "subVendorStoryboard", bundle: nil).instantiateViewController(withIdentifier: "subVendorListingViewController") as! subVendorListingViewController
            
            navigation.pushViewController(vc, animated: true)
            
        }
        */
        
    }
    
    
}
extension Array where Element: Hashable {
    func removingDuplicates() -> [Element] {
        var addedDict = [Element: Bool]()
        
        return filter {
            addedDict.updateValue(true, forKey: $0) == nil
        }
    }
    
    mutating func removeDuplicates() {
        self = self.removingDuplicates()
    }
}
