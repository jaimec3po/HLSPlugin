//
//  HLSPlayerPlugin.m
//  SenalRadionica
//
//  Created by Jaime Caicedo on 2/3/14.
//
//

#import "HLSPlugin.h"

@implementation HLSPlugin

- (void)create:(CDVInvokedUrlCommand*)command{
	NSLog(@"%@", @"HLSPlugin create");
}

- (void)getCurrentPositionAudio:(CDVInvokedUrlCommand*)command{
	NSLog(@"%@", @"HLSPlugin getCurrentPosition");
}

- (void)startPlayingAudio:(CDVInvokedUrlCommand*)command{
	NSLog(@"%@", @"HLSPlugin startPlayingAudio");
}

- (void)stopPlayingAudio:(CDVInvokedUrlCommand*)command{
	NSLog(@"%@", @"HLSPlugin stopPlayinAudio");
}

- (void)setVolume:(CDVInvokedUrlCommand*)command{
	NSLog(@"%@", @"HLSPlugin setVolume");
}



@end
