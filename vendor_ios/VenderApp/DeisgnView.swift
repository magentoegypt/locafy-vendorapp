//
//  DeisgnView.swift
//  layer
//
//  Created by Macmini on 15/12/17.
//  Copyright © 2017 Macmini. All rights reserved.
//

import UIKit
@IBDesignable
class DeisgnViewButton: UIButton{
    @IBInspectable var cornerRadius: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    
    
    
    
    //layer.borderColor
    @IBInspectable var borderColor: UIColor = .black{
        didSet{
            setupView()
        }
    }
    ///layer.borderWidth
    @IBInspectable var borderWidth: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    //
    
    override func prepareForInterfaceBuilder() {
        setupView()
    }
    @objc func setupView()
    {
        layer.cornerRadius=cornerRadius
        layer.borderColor=borderColor.cgColor
        layer.borderWidth=borderWidth
    }
}



class DeisgnViewLabel: UILabel{
    @IBInspectable var cornerRadius: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    
    //layer.borderColor
    @IBInspectable var borderColor: UIColor = .black{
        didSet{
            setupView()
        }
    }
    ///layer.borderWidth
    @IBInspectable var borderWidth: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    //
    
    override func prepareForInterfaceBuilder() {
        setupView()
    }
    @objc func setupView()
    {
        layer.cornerRadius=cornerRadius
        layer.borderColor=borderColor.cgColor
        layer.borderWidth=borderWidth
    }
}



class DeisgnViewUIView: UIView{
    @IBInspectable var cornerRadius: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    
    //layer.borderColor
    @IBInspectable var borderColor: UIColor = .black{
        didSet{
            setupView()
        }
    }
    ///layer.borderWidth
    @IBInspectable var borderWidth: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    //
    
    override func prepareForInterfaceBuilder() {
        setupView()
    }
    @objc func setupView()
    {
        layer.cornerRadius=cornerRadius
        layer.borderColor=borderColor.cgColor
        layer.borderWidth=borderWidth
    }
}

class DeisgnViewSegmentedControl: UISegmentedControl{
    
    @IBInspectable var cornerRadius: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    
    //layer.borderColor
    @IBInspectable var borderColor: UIColor = .black{
        didSet{
            setupView()
        }
    }
    ///layer.borderWidth
    @IBInspectable var borderWidth: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    //
    
    override func prepareForInterfaceBuilder() {
        setupView()
    }
    @objc func setupView()
    {
        layer.cornerRadius=cornerRadius
        layer.borderColor=borderColor.cgColor
        layer.borderWidth=borderWidth
    }
}


class DeisgnViewTextField: UITextField{
    
    @IBInspectable var cornerRadius: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    
    //layer.borderColor
    @IBInspectable var borderColor: UIColor = .black{
        didSet{
            setupView()
        }
    }
    ///layer.borderWidth
    @IBInspectable var borderWidth: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    //
    
    override func prepareForInterfaceBuilder() {
        setupView()
    }
    @objc func setupView()
    {
        layer.cornerRadius=cornerRadius
        layer.borderColor=borderColor.cgColor
        layer.borderWidth=borderWidth
    }
}

class DeisgnViewSlider: UISlider{
    
    @IBInspectable var cornerRadius: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    
    //layer.borderColor
    @IBInspectable var borderColor: UIColor = .black{
        didSet{
            setupView()
        }
    }
    ///layer.borderWidth
    @IBInspectable var borderWidth: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    //
    
    override func prepareForInterfaceBuilder() {
        setupView()
    }
    @objc func setupView()
    {
        layer.cornerRadius=cornerRadius
        layer.borderColor=borderColor.cgColor
        layer.borderWidth=borderWidth
    }
}


class DeisgnViewSwitch: UISwitch{
    
    @IBInspectable var cornerRadius: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    
    //layer.borderColor
    @IBInspectable var borderColor: UIColor = .black{
        didSet{
            setupView()
        }
    }
    ///layer.borderWidth
    @IBInspectable var borderWidth: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    //
    
    override func prepareForInterfaceBuilder() {
        setupView()
    }
    @objc func setupView()
    {
        layer.cornerRadius=cornerRadius
        layer.borderColor=borderColor.cgColor
        layer.borderWidth=borderWidth
    }
}

class DeisgnViewUIProgressView: UIProgressView{
    
    @IBInspectable var cornerRadius: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    
    //layer.borderColor
    @IBInspectable var borderColor: UIColor = .black{
        didSet{
            setupView()
        }
    }
    ///layer.borderWidth
    @IBInspectable var borderWidth: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    //
    
    override func prepareForInterfaceBuilder() {
        setupView()
    }
    @objc func setupView()
    {
        layer.cornerRadius=cornerRadius
        layer.borderColor=borderColor.cgColor
        layer.borderWidth=borderWidth
    }
}

class DeisgnViewUIPageControl: UIPageControl{
    
    @IBInspectable var cornerRadius: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    
    //layer.borderColor
    @IBInspectable var borderColor: UIColor = .black{
        didSet{
            setupView()
        }
    }
    ///layer.borderWidth
    @IBInspectable var borderWidth: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    //
    
    override func prepareForInterfaceBuilder() {
        setupView()
    }
    @objc func setupView()
    {
        layer.cornerRadius=cornerRadius
        layer.borderColor=borderColor.cgColor
        layer.borderWidth=borderWidth
    }
}
class DeisgnViewUIStepper: UIStepper{
    
    @IBInspectable var cornerRadius: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    
    //layer.borderColor
    @IBInspectable var borderColor: UIColor = .black{
        didSet{
            setupView()
        }
    }
    ///layer.borderWidth
    @IBInspectable var borderWidth: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    //
    
    override func prepareForInterfaceBuilder() {
        setupView()
    }
    @objc func setupView()
    {
        layer.cornerRadius=cornerRadius
        layer.borderColor=borderColor.cgColor
        layer.borderWidth=borderWidth
    }
}
class DeisgnViewUIImageView: UIImageView{
    
    @IBInspectable var cornerRadius: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    
    //layer.borderColor
    @IBInspectable var borderColor: UIColor = .black{
        didSet{
            setupView()
        }
    }
    ///layer.borderWidth
    @IBInspectable var borderWidth: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    //
    
    override func prepareForInterfaceBuilder() {
        setupView()
    }
    @objc func setupView()
    {
        layer.cornerRadius=cornerRadius
        layer.borderColor=borderColor.cgColor
        layer.borderWidth=borderWidth
    }
}
class DeisgnViewUITextView: UITextView{
    
    @IBInspectable var cornerRadius: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    
    //layer.borderColor
    @IBInspectable var borderColor: UIColor = .black{
        didSet{
            setupView()
        }
    }
    ///layer.borderWidth
    @IBInspectable var borderWidth: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    //
    
    override func prepareForInterfaceBuilder() {
        setupView()
    }
    @objc func setupView()
    {
        layer.cornerRadius=cornerRadius
        layer.borderColor=borderColor.cgColor
        layer.borderWidth=borderWidth
    }
}

class DeisgnViewUIDatePicker: UIDatePicker{
    
    @IBInspectable var cornerRadius: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    
    //layer.borderColor
    @IBInspectable var borderColor: UIColor = .black{
        didSet{
            setupView()
        }
    }
    ///layer.borderWidth
    @IBInspectable var borderWidth: CGFloat = 0{
        didSet{
            setupView()
        }
    }
    //
    
    override func prepareForInterfaceBuilder() {
        setupView()
    }
    @objc func setupView()
    {
        layer.cornerRadius=cornerRadius
        layer.borderColor=borderColor.cgColor
        layer.borderWidth=borderWidth
    }
}
