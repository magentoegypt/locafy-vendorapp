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

class ced_addAttrfirstcell: UITableViewCell,UITextViewDelegate {

    //labels start
    @IBOutlet weak var attributeCodeLbl: UILabel!
    @IBOutlet weak var scopeLabel: UILabel!
    @IBOutlet weak var catalogInputLabel: UILabel!
    @IBOutlet weak var defaultValuelabel: UILabel!
    @IBOutlet weak var uniquevallabel: UILabel!
    @IBOutlet weak var valRequiredLabel: UILabel!
    @IBOutlet weak var InputValidationForStoreLabel: UILabel!
    @IBOutlet weak var applyTolabel: UILabel!
    
    //end
    @IBOutlet weak var attriButeCode: UITextView!
    @IBOutlet weak var selectScope: UIButton!
    @IBOutlet weak var selectCatalogInputtype: UIButton!
    @IBOutlet weak var defaultValue: UITextView!
    @IBOutlet weak var selectuniqueVal: UIButton!
    @IBOutlet weak var valuesRequired: UIButton!
    @IBOutlet weak var selectInputvalidation: UIButton!    
    @IBOutlet weak var selectApplyTo: UIButton!
    
    //Variables 
    var tableView = UITableView()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        selectScope.addTarget(self, action: #selector(ced_addAttrfirstcell.selectedScope(_:)), for: UIControl.Event.touchUpInside)
        selectCatalogInputtype.addTarget(self, action: #selector(ced_addAttrfirstcell.selectcatalog(_:)), for: UIControl.Event.touchUpInside)
        selectuniqueVal.addTarget(self, action: #selector(ced_addAttrfirstcell.selectyes(_:)), for: UIControl.Event.touchUpInside)
        valuesRequired.addTarget(self, action: #selector(ced_addAttrfirstcell.selectyes(_:)), for: UIControl.Event.touchUpInside)
        selectInputvalidation.addTarget(self, action: #selector(selectInputValidation(_:)), for: UIControl.Event.touchUpInside);
        attriButeCode.delegate = self
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }
    
    @objc func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        let size = attriButeCode.contentSize.height/(attriButeCode.font?.lineHeight)!
        
        let sized = CGSize(width: attriButeCode.contentSize.width, height: size)
        attriButeCode.sizeThatFits(sized)
        attriButeCode.sizeToFit()
        self.contentView.autoresizesSubviews = true
        self.setNeedsLayout()
        self.layoutIfNeeded()
        return true
    }
  
    @objc func selectedScope(_ sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = ["Select","Store View","Website","Global"];
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
           
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
    @objc func selectcatalog(_ sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = ["Text Field","Text Area","Date","Yes/No","Multiple Select","Dropdown","Price","Media Image"];
        
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            self.UpdateInfor(sender)
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
    @objc func selectyes(_ sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = ["Select","Yes","No"];
        
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
    @objc func selectInputValidation(_ sender:UIButton){
        
    }
    
    @objc func UpdateInfor(_ sender:UIButton){
        let cell = tableView.cellForRow(at: IndexPath(row: 0, section: 0)) as! ced_addAttrfirstcell
        if(sender.titleLabel?.text == "Text Area"){
            tableView.beginUpdates()
            cell.InputValidationForStoreLabel.frame.size = CGSize(width: 0, height: 0)
            cell.selectInputvalidation.frame.size = CGSize(width: 0, height: 0)
            let view = UIView(frame: CGRect(x: 0, y: cell.frame.height - 40, width: cell.frame.width , height: 40))
            view.backgroundColor = UIColor.green
            cell.contentView.addSubview(view)
            cell.updateConstraintsIfNeeded()
            tableView.reloadSections(IndexSet(integer: 0), with: UITableView.RowAnimation.automatic)
            tableView.endUpdates()
            
        }
    }

    @objc func textViewDidChange(_ textView: UITextView) {
        tableView.beginUpdates()
        tableView.endUpdates()
    }
    
    
}
