//
//  SSRadioButton.swift
//  VenderApp
//
//  Created by Apple on 14/04/2024.
//  Copyright © 2024 CEDCOSS Technologies Private Limited. All rights reserved.
//

import Foundation
import UIKit
@IBDesignable

class SSRadioButton: UIButton {
    
    fileprivate var circleLayer = CAShapeLayer()
    fileprivate var fillCircleLayer = CAShapeLayer()
    override var isSelected: Bool {
        didSet {
            toggleButon()
        }
    }
    /**
     Color of the radio button circle. Default value is UIColor red.
     */
    @IBInspectable var circleColor: UIColor = UIColor.red {
        didSet {
            circleLayer.strokeColor = strokeColor.cgColor
            self.toggleButon()
        }
    }
    
    /**
     Color of the radio button stroke circle. Default value is UIColor red.
     */
    @IBInspectable var strokeColor: UIColor = UIColor.gray {
        didSet {
            circleLayer.strokeColor = strokeColor.cgColor
            self.toggleButon()
        }
    }
    
    /**
     Radius of RadioButton circle.
     */
    @IBInspectable var circleRadius: CGFloat = 5.0
    @IBInspectable var cornerRadius: CGFloat {
        get {
            return layer.cornerRadius
        }
        set {
            layer.cornerRadius = newValue
            layer.masksToBounds = newValue > 0
        }
    }
    
    fileprivate func circleFrame() -> CGRect {
        var circleFrame = CGRect(x: 0, y: 0, width: 2*circleRadius, height: 2*circleRadius)
        circleFrame.origin.x = 0 + circleLayer.lineWidth
        circleFrame.origin.y = bounds.height/2 - circleFrame.height/2
        return circleFrame
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        initialize()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        initialize()
    }
    
    fileprivate func initialize() {
        circleLayer.frame = bounds
        circleLayer.lineWidth = 2
        circleLayer.fillColor = UIColor.clear.cgColor
        circleLayer.strokeColor = strokeColor.cgColor
        layer.addSublayer(circleLayer)
        fillCircleLayer.frame = bounds
        fillCircleLayer.lineWidth = 2
        fillCircleLayer.fillColor = UIColor.clear.cgColor
        fillCircleLayer.strokeColor = UIColor.clear.cgColor
        layer.addSublayer(fillCircleLayer)
        self.titleEdgeInsets = UIEdgeInsets(top: 0, left: (4*circleRadius + 4*circleLayer.lineWidth), bottom: 0, right: 0)
        self.toggleButon()
    }
    /**
     Toggles selected state of the button.
     */
    func toggleButon() {
        if self.isSelected {
            fillCircleLayer.fillColor = circleColor.cgColor
            circleLayer.strokeColor = circleColor.cgColor
        } else {
            fillCircleLayer.fillColor = UIColor.clear.cgColor
            circleLayer.strokeColor = strokeColor.cgColor
        }
    }
    
    fileprivate func circlePath() -> UIBezierPath {
        return UIBezierPath(ovalIn: circleFrame())
    }
    
    fileprivate func fillCirclePath() -> UIBezierPath {
        return UIBezierPath(ovalIn: circleFrame().insetBy(dx: 2, dy: 2))
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        circleLayer.frame = bounds
        circleLayer.path = circlePath().cgPath
        fillCircleLayer.frame = bounds
        fillCircleLayer.path = fillCirclePath().cgPath
        self.titleEdgeInsets = UIEdgeInsets(top: 0, left: (2*circleRadius + 4*circleLayer.lineWidth), bottom: 0, right: 0)
    }
    
    override func prepareForInterfaceBuilder() {
        initialize()
    }
}


@IBDesignable
class RadioButtonControl: UIControl {

    // MARK: - Properties
    private let radioImageView = UIImageView()
    private let titleLabel = UILabel()

    @IBInspectable var title: String = "Radio Button" {
        didSet { titleLabel.text = title }
    }

    @IBInspectable var isSelectedRadio: Bool = false {
        didSet { updateUI() }
    }

    @IBInspectable var selectedColor: UIColor = UIColor(hexString: "#3F89E6")!
    @IBInspectable var unselectedColor: UIColor = UIColor(hexString: "#3F89E6")!
    @IBInspectable var textColor: UIColor = .black

    var onValueChanged: ((RadioButtonControl) -> Void)?
    
    // MARK: - Initialization
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }

    private func setupView() {
        // Radio Button Image
        radioImageView.contentMode = .scaleAspectFit
        updateUI()
        
        // Title Label
        titleLabel.text = title
        titleLabel.font = UIFont.systemFont(ofSize: 16)
        titleLabel.textColor = textColor
        titleLabel.numberOfLines = 0
        
        // Gesture Recognizer
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(selectRadio))
        addGestureRecognizer(tapGesture)

        // Layout
        let stackView = UIStackView(arrangedSubviews: [radioImageView, titleLabel])
        stackView.axis = .horizontal
        stackView.spacing = 8
        stackView.alignment = .center
        stackView.translatesAutoresizingMaskIntoConstraints = false
        addSubview(stackView)

        NSLayoutConstraint.activate([
            stackView.leadingAnchor.constraint(equalTo: self.leadingAnchor, constant: 0),
            stackView.trailingAnchor.constraint(equalTo: self.trailingAnchor, constant: 0),
            stackView.topAnchor.constraint(equalTo: self.topAnchor, constant: 10),
            stackView.bottomAnchor.constraint(equalTo: self.bottomAnchor, constant: -10),
            
            radioImageView.widthAnchor.constraint(equalToConstant: 24),
            radioImageView.heightAnchor.constraint(equalToConstant: 24)
        ])
    }

    // MARK: - UI Update
    private func updateUI() {
        let imageName = isSelectedRadio ? "largecircle.fill.circle" : "circle"
        radioImageView.image = UIImage(systemName: imageName)
        radioImageView.tintColor = isSelectedRadio ? selectedColor : unselectedColor
    }

    // MARK: - Select Action
    @objc private func selectRadio() {
        guard !isSelectedRadio else { return } // Avoid redundant updates
        isSelectedRadio = true
        sendActions(for: .valueChanged)  // Notify listeners
        onValueChanged?(self)
    }
}


private var ActionKey: UInt8 = 0

extension UIButton {

    private class ActionWrapper {
        let action: () -> Void
        init(action: @escaping () -> Void) {
            self.action = action
        }
    }

    func setAction(_ action: @escaping () -> Void) {
        objc_setAssociatedObject(self, &ActionKey, ActionWrapper(action: action), .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        addTarget(self, action: #selector(triggerAction), for: .touchUpInside)
    }

    @objc private func triggerAction() {
        if let wrapper = objc_getAssociatedObject(self, &ActionKey) as? ActionWrapper {
            wrapper.action()
        }
    }
}

// Adding a closure as target to a UIButton
class ClosureSleeve {
    let closure: ()->()
    
    init (_ closure: @escaping ()->()) {
        self.closure = closure
    }
    
    @objc func invoke () {
        closure()
    }
}

typealias UIControlEvents = UIControl.Event

extension UIControl {
    func add (for controlEvents: UIControlEvents, _ closure: @escaping ()->()) {
        let sleeve = ClosureSleeve(closure)
        addTarget(sleeve, action: #selector(ClosureSleeve.invoke), for: controlEvents)
        objc_setAssociatedObject(self, String(ObjectIdentifier(self).hashValue) + String(controlEvents.rawValue), sleeve,
                             objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN)
    }
}


class LabelButtonView: UIView {

    let label: UILabel = {
        let lbl = UILabel()
        lbl.text = "Label"
        lbl.numberOfLines = 0
        lbl.textColor = .black
        lbl.font = UIFont.systemFont(ofSize: 12)
        lbl.translatesAutoresizingMaskIntoConstraints = false
        return lbl
    }()

    let button: UIButton = {
        let btn = UIButton(type: .system)
        btn.setTitle("Button", for: .normal)
        btn.titleLabel?.font = UIFont.systemFont(ofSize: 15)
        btn.translatesAutoresizingMaskIntoConstraints = false
        return btn
    }()

    let stackView: UIStackView = {
            let stack = UIStackView()
            stack.axis = .vertical
            stack.spacing = 5
        stack.alignment = .leading // center horizontally
            stack.distribution = .equalSpacing
            stack.translatesAutoresizingMaskIntoConstraints = false
            return stack
        }()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }

    private func setupView() {
        addSubview(label)
        addSubview(button)
        addSubview(stackView)

        NSLayoutConstraint.activate([
            label.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 16),
          //  label.centerYAnchor.constraint(equalTo: centerYAnchor, constant: -20),
            label.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -16),
            button.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -16),
            button.topAnchor.constraint(equalTo: label.bottomAnchor, constant: 5),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 16),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -16),
            stackView.topAnchor.constraint(equalTo: button.bottomAnchor, constant: 5)
        ])
    }
}
