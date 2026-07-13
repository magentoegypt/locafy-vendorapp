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

class ced_buttoncell: UITableViewCell,UITextViewDelegate {
    @IBOutlet weak var Label: UILabel!
    @IBOutlet weak var Button: UIButton!
    var tableView  = UITableView()
    var parent     = UIViewController()
    var data  = [String:String]()
    let defaults = UserDefaults.standard
    
    
    @IBOutlet weak var l1: UILabel!
    @IBOutlet weak var l2: UILabel!
    @IBOutlet weak var l3: UILabel!
    @IBOutlet weak var l4: UILabel!
    @IBOutlet weak var l5: UILabel!
    @IBOutlet weak var l6: UILabel!
    
    @IBOutlet weak var l7: UILabel!
    @IBOutlet weak var l8: UILabel!
    @IBOutlet weak var l9: UILabel!
    @IBOutlet weak var l10: UILabel!
    
    
    
    
    @IBOutlet weak var attributeCode: UITextView!
    @IBOutlet weak var scope: UIButton!
    @IBOutlet weak var catalog: UIButton!
    @IBOutlet weak var defaultValue: UITextView!
    @IBOutlet weak var uniqueValue: UIButton!
    @IBOutlet weak var valuesRequired: UIButton!
    @IBOutlet weak var inputvalidationStore: UIButton!
    @IBOutlet weak var applyTo: UIButton!
    @IBOutlet weak var usetocreateconfig: UIButton!
    @IBOutlet weak var defaultyesNo: UIButton!
    
    @IBOutlet weak var useInFilterOption: UIButton!
    
    
    var storeDropdownArray = [String]()
    var yesNoDropdownArray = [String]()
    var scopeDropdownArray = [String]()
    var frontendDropdownArray = [String]()
    var inputTypeDropdownArray = [String]()
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        
        applyTo.setTitleColor( .white, for: .normal)
        useInFilterOption.setTitleColor(.white, for: .normal)
        usetocreateconfig.setTitleColor(.white, for: .normal)
        
        scope.addTarget(self, action: #selector(ced_buttoncell.selectOptions(_:)), for: UIControl.Event.touchUpInside)
        catalog.addTarget(self, action: #selector(ced_buttoncell.selectOptions(_:)), for: UIControl.Event.touchUpInside)
        uniqueValue.addTarget(self, action: #selector(ced_buttoncell.selectOptions(_:)), for: UIControl.Event.touchUpInside)
        valuesRequired.addTarget(self, action: #selector(ced_buttoncell.selectOptions(_:)), for: UIControl.Event.touchUpInside)
        inputvalidationStore.addTarget(self, action: #selector(ced_buttoncell.selectOptions(_:)), for: UIControl.Event.touchUpInside)
        applyTo.addTarget(self, action: #selector(ced_buttoncell.selectOptions(_:)), for: UIControl.Event.touchUpInside)
        usetocreateconfig.addTarget(self, action: #selector(ced_buttoncell.selectOptions(_:)), for: UIControl.Event.touchUpInside)
        defaultValue.delegate = self
        defaultyesNo.addTarget(self, action: #selector(ced_buttoncell.selectOptions(_:)), for: UIControl.Event.touchUpInside)
         useInFilterOption.addTarget(self, action: #selector(ced_buttoncell.selectOptions(_:)), for: UIControl.Event.touchUpInside)
        
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }
    
    @objc func selectOptions(_ sender:UIButton){
        let dropDown = DropDown();
        if(sender == catalog){
            dropDown.dataSource = inputTypeDropdownArray
            print(inputTypeDropdownArray)
      //      dropDown.dataSource = ["Select","Text Field","Text Area","Date","Yes/No","Multiple Select","Dropdown","Price","Media Image"];
        }else if(sender == scope){
            dropDown.dataSource = scopeDropdownArray
          //  dropDown.dataSource = ["Select","Store View","Website","Global"];
        }else if(sender == inputvalidationStore){
            dropDown.dataSource = frontendDropdownArray
          //  dropDown.dataSource = ["None","Decimal Number","Integer Number","Email","URL","Letters","Letters (a-z, A-Z) or Numbers (0-9)"];
        }//else if(sender == applyTo){
//            dropDown.dataSource = ["All Product Types","Selected Product Types"];
//        }
        else{
            dropDown.dataSource = yesNoDropdownArray
            //dropDown.dataSource = ["Select","Yes","No"];
        }
        dropDown.selectionAction = {(index, item) in
            print(item)
            sender.setTitle(item, for: UIControl.State());
            self.updateSection(item,sender: sender)
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
        
    }
    
    @objc func updateSection(_ str:String,sender:UIButton){
        if(str != "Yes/No" && sender == catalog){
            defaultyesNo.isHidden = true
            defaultValue.isHidden = false
        }
        if(str != "Media Image" && sender == catalog){
            defaultValue.isEditable = true
            defaultValue.alpha = 1
            uniqueValue.isEnabled = true
            uniqueValue.alpha = 1
            valuesRequired.isEnabled = true
            valuesRequired.alpha = 1
            inputvalidationStore.isEnabled = true
            inputvalidationStore.alpha = 1
            usetocreateconfig.isEnabled = true
            usetocreateconfig.alpha = 1
          //  tableView.reloadData()
        }
        let view2=ced_addATTrbutenew()
        //let view2 = parent as! ced_addATTrbutenew
        if(str == "Selected Product Types"){
            view2.applyTo = "seletedcat"
            tableView.reloadSections(IndexSet(integer: 1), with: UITableView.RowAnimation.fade)
        }else if(str.lowercased() == "All product types".lowercased()){
            view2.applyTo = ""
            tableView.reloadSections(IndexSet(integer: 1), with: UITableView.RowAnimation.fade)
            
        }
        if(str == "All Product Types"){
            let view2=ced_addATTrbutenew()
            view2.applyTo = ""
            tableView.reloadData()
            // tableView.reloadRowsAtIndexPaths([NSIndexPath(forRow: 7, inSection: 0)], withRowAnimation: UITableViewRowAnimation.Automatic)
        }
        if(str == "Text Area"){
            let view2=ced_addATTrbutenew()
            view2.selectedcatalog = "Text Area"
            tableView.cellForRow(at: IndexPath(row: 6, section: 0))?.isHidden = true
            usetocreateconfig.isEnabled = false
            usetocreateconfig.alpha = 0.5
            inputvalidationStore.isEnabled = false
            inputvalidationStore.alpha = 0.5
           // tableView.reloadData()
        }else if(str == "Text Field"){
            let view=ced_addATTrbutenew()
            view.selectedcatalog = "Text Field"
            usetocreateconfig.isEnabled = false
            usetocreateconfig.alpha = 0.5
            defaultValue.isEditable = true
            defaultValue.alpha = 1
            inputvalidationStore.isEnabled = true
            inputvalidationStore.alpha = 1
            uniqueValue.alpha = 1
            uniqueValue.isEnabled = true
            scope.isEnabled = true
            scope.alpha = 1
            
          //  tableView.reloadData()
            
        }else  if(str == "Date"){
            
            defaultValue?.delegate = self
            usetocreateconfig.isEnabled = false
            usetocreateconfig.alpha = 0.5
            inputvalidationStore.isEnabled = false
            inputvalidationStore.alpha = 0.5
        }else  if(str == "Yes/No"){
            defaultyesNo.isHidden = false
            defaultValue.isHidden = true
            inputvalidationStore.isEnabled = false
            inputvalidationStore.alpha = 0.5
        }
        else  if(str == "Multiple Select"){
            
            let view=ced_addATTrbutenew()
            view.textareaSelected = "Multiple Select"
            view.selectedcatalog = "Multiple Select"
            usetocreateconfig.isEnabled = false
            usetocreateconfig.alpha = 0.5
            inputvalidationStore.isEnabled = false
            inputvalidationStore.alpha = 0.5
            
            inputvalidationStore.isEnabled = false
            inputvalidationStore.alpha = 0.5
            defaultValue.alpha = 0.5
            defaultValue.isEditable = false
         //   tableView.reloadData()
        }
        else  if(str == "Dropdown"){
            
            let view=ced_addATTrbutenew()
            view.textareaSelected = "Dropdown"
            view.selectedcatalog = "Dropdown"
            usetocreateconfig.isEnabled = false
            usetocreateconfig.alpha = 0.5
            if(scope.titleLabel!.text == "Global"){
                usetocreateconfig.isEnabled = true
                usetocreateconfig.alpha = 1
            }
            inputvalidationStore.isEnabled = false
            inputvalidationStore.alpha = 0.5
            inputvalidationStore.isEnabled = false
            inputvalidationStore.alpha = 0.5
            defaultValue.alpha = 0.5
            defaultValue.isEditable = false
          //  tableView.reloadData()
        }
        else  if(str == "Price"){
            let view=ced_addATTrbutenew()
            view.selectedcatalog = "Price"
         //   tableView.reloadData()
            defaultValue.isEditable = true
            defaultValue.alpha = 1
            scope.alpha = 0.5
            scope.isEnabled = false
            
        }
        else  if(str == "Media Image"){
            let view=ced_addATTrbutenew()
            view.selectedcatalog = "Media Image"
            defaultValue.isEditable = false
            defaultValue.alpha = 0.5
            uniqueValue.isEnabled = false
            uniqueValue.alpha = 0.5
            valuesRequired.isEnabled = false
            valuesRequired.alpha = 0.5
            inputvalidationStore.isEnabled = false
            inputvalidationStore.alpha = 0.5
            usetocreateconfig.isEnabled = false
            usetocreateconfig.alpha = 0.5
       //     tableView.reloadData()
        }
        
        
        
        ///Adding the add admin button
        
        
    }
    
    @objc func textViewDidBeginEditing(_ textView: UITextView) {
        if(catalog.titleLabel?.text == "Date"){
            let dateFormatter = DateFormatter()
            dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        //    dateFormatter.dateStyle = NSDateFormatterStyle.FullStyle
            dateFormatter.dateFormat = "yyyy-MM-dd"
           // dateFormatter.timeStyle = NSDateFormatterStyle.NoStyle
            
            DatePickerDialog().show("Select Date", doneButtonTitle: "Done", cancelButtonTitle: "Cancel", datePickerMode: UIDatePicker.Mode.date) {
                (date) -> Void in
                print(date)
                self.defaultValue.text = dateFormatter.string(from: date);
            }
        }
    }
    
}

extension Dictionary {
    mutating func update(_ other:Dictionary) {
        for (key,value) in other {
            self.updateValue(value, forKey:key)
        }
    }
}
